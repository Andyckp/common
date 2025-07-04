# Derivative Pricer 

An event-driven system for real-time derivative pricing.

## Key Features

- **Reactive**  
  Responds to market data and volatility surface updates, repricing only dependent instruments.

- **Low Latency**  
  Built for speed using [Aeron](https://github.com/real-logic/aeron) for transport and [SBE](https://github.com/real-logic/simple-binary-encoding) for compact binary encoding.

- **Low-GC Design**  
  Object reuse and preallocated buffers ensure minimal garbage collection and consistent performance.

## Topology

![Component Diagram](https://github.com/Andyckp/common/blob/master/derivativepricer/derivativepricer.drawio.svg)

### Components

- **Aeron and SBE Input/Output Adaptors**  
  Connect to Aeron streams and perform SBE encoding/decoding.

- **Ringbuffers**  
  Thread-safe, preallocated buffers for inter-process communication without object allocation.

- **Multi-Partitioned Primary Greek Valuation**  
  Performs CPU-intensive theo, delta, and gamma calculations via a quantitative library.  
  Workloads are partitioned by `underlying_id`, each handled by a dedicated thread for horizontal scaling.  
  Rapid input ticks are conflated to compute once per burst, optimizing throughput during peak hours.  
  Primary greeks are multicasted to downstream applications via Aeron and cascaded to the adjustment process via ringbuffer.

- **Multi-Partitioned Secondary Greek Valuation**  
  Handles all other greeks using the same partitioning and conflation strategy as primary valuation.

- **Adjusted Theo Valuation**  
  Reacts to market data and cached primary greeks to compute fast theo via delta-gamma adjustment.

## Input Data Overview

- **Instrument**: `(instrument_id, FK: underlying_id)`
- **Strategy**: `(strategy_id, FK: underlying_id, volatility_id, market_data_id)`
- **Volatility**: `(volatility_id)`
- **Market Data**: `(market_data_id)` — e.g. underlying price

A strategy selects instruments via `underlying_id` and pricing inputs via `volatility_id` and `market_data_id`.  
Each instrument can have different greeks per strategy, based on its assigned volatility and market data.

## Output Data Overview

- **Primary Greeks**: Contains major greeks (theo, delta, gamma) for fast response.
- **Secondary Greeks**: Contains Other greeks calculated by the quantitative library.
- **Adjusted Greeks**: Very fast theo via delta-gamma adjustment on market data changes.

## Integration Test

This is the best entry point to explore the system:

- [ApplicationIntegrationTest.java](https://github.com/Andyckp/common/blob/master/derivativepricer/src/test/java/com/ac/derivativepricer/ApplicationIntegrationTest.java)  
  - Starts the application  
  - Publishes market data and verifies only dependent instruments' greeks are published  
  - Publishes volatility and verifies correct downstream propagation

## Main Classes Overview
- [adaptor](https://github.com/Andyckp/common/tree/master/derivativepricer/src/main/java/com/ac/derivativepricer/adaptor)

- [process](https://github.com/Andyckp/common/tree/master/derivativepricer/src/main/java/com/ac/derivativepricer/business)

- [data structure](https://github.com/Andyckp/common/tree/master/derivativepricer/src/main/java/com/ac/derivativepricer/data)
- [SBE codec](https://github.com/Andyckp/common/tree/master/derivativepricer/src/main/java/com/ac/derivativepricer/codec)

## Next Steps

### Implementation

- Add sequence number in SBE schema  
- Subscribe to capture stream only after replay stream finishes  
- Use separate Aeron managers for main and test environments  
- Implement delta-gamma adjustment  
- Support remapping of strategy to market data and to volatility at runtime

### Performance

- Use `int` instead of `LocalDate` in business processes 
- Reuse `char[]` from `CharArrayKey` in business processes
- Use padding to avoid false sharing  
