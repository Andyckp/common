# Common Java Libraries (Experimental)  

## Overview  
`common` is a collection of **reusable Java libraries**, currently in an **experimental stage**, designed for **high-performance applications**. It explores **high-throughput** and **low-latency** techniques, focusing on efficient data processing and concurrency.  

## Key Experimental Features  
- **Subscribable Cache** – A data-driven integration mechanism for composable sub-systems.  
- **Dispatch System** – A flexible threading model optimized for high-throughput execution.  
- **Threading Model** – Supports parallel and serial execution strategies for efficient workload distribution.  

**Note:** This project is still **experimental** and subject to changes

---
# Subscribable Cache  

## Overview  
The **Subscribable Cache** is a **data-driven integration mechanism** designed to facilitate **composable sub-systems** within an application. It serves as a **communication interface** for both **cross-application** and **cross-module** interactions.  
See https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/subscribeable

## Features  
- **Data-Driven Design** – Enables efficient, event-driven updates.  
- **Composable Architecture** – Acts as an integration point for modular system design.  
- **Cross-Application & Cross-Module Communication** – Provides a unified interface for seamless data exchange.  
- **Connected Graph of Caches & Processors** – Forms a structured pipeline for data processing.  

## Processing Flow  
Message System / Database → Cache → Processor → Cache → Processor → ... → Cache → Message System / Database
This **cyclic data flow** ensures **efficient data propagation** across different components.  

## Reference  
[SubscribableCache.java](https://github.com/Andyckp/common/blob/master/dispatch/src/main/java/com/ac/common/subscribeable/SubscribableCache.java)

---
# Dispatch   

## Overview  
This high-throughput threading model optimizes **message processing** by balancing **serial execution** and **parallelism** while preventing system overload.  
See https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/dispatch. 

## Features  
- **Serial Execution** – Ensures ordered processing for the same `PartitionKey`.  
- **Parallel Execution** – Allows concurrent processing across different `PartitionKey` values.  
- **Conflation** – Merges redundant messages to reduce workload.  
- **Grace Period** – Prevents flooding by introducing controlled delays.  

## Implementation  
- Accepts a **thread pool** (`Map<PartitionKey, MessageProcessorExecutor>`), matching CPU count.  
- Uses **shared worker threads** for efficient resource utilization.  
- Guarantees **serial execution** within a partition while enabling **parallel execution** across partitions.  

## Reference  
[MessageProcessExecutor.java](https://github.com/Andyckp/common/blob/master/dispatch/src/main/java/com/ac/common/dispatch/MessageProcessExecutor.java)

---

# Dispatch 2 (Experimental)  

## Overview  
`dispatch2` is an **experimental low-latency threading model** designed to explore **high-performance concurrency techniques**. It focuses on optimizing message processing using **Disruptor** for efficient event-driven execution.  

## Key Experimental Features  
- **Low-Latency Threading Model** – Designed for minimal overhead in concurrent processing.  
- **Disruptor Integration** – Investigates the use of Disruptor for high-speed message passing.  

## Status  
**This project is not yet complete** and remains an **ongoing exploration** of advanced threading techniques. Further development and optimizations are planned.  

## Reference  
[Disruptor Implementation](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/disruptor)  
[Dispatch 2](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/dispatch2)  
