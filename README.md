# Common Java Libraries  

## Overview  
The `common` project is designed to **explore, learn, and build** a collection of **reusable Java libraries**. It focuses on **subscribable caches**, **high-throughput and low-latency multithreading patterns**, and **Aeron transport with SBE codec**.  

## Key Components  
- **Subscribable Cache** – A key-value map that supports subscriptions, enabling easy composition of modular systems. This component is **stable** and captures learned best practices.  
- **High-Throughput Threading** – A multithreading toolset focused on **data partitioning**. This component is **stable** and captures learned best practices.  
- **Low-Latency Threading** – A threading toolset optimized for **low garbage collection (GC)**. This component is **experimental** and explores new possibilities.  
- **Aeron Transport & SBE Codec** – Investigates the use of **Aeron** for high-performance transport and **SBE** for efficient binary encoding.  

---

# Subscribable Cache  

## Overview  
The **Subscribable Cache** serves as a **unified communication interface** for both **cross-application** and **cross-module interactions within an application**, enabling easy composition of standardized processing models.  

## Processing Flow  
Message System / Database → Cache → Processor → Cache → Processor → ... → Cache → Message System / Database

This **cyclic data flow** ensures **efficient data propagation** across different components.  

## Features  
- **Data-Driven Design** – Enables efficient, event-driven updates.  
- **Composable Architecture** – Acts as an integration point for modular system design.  
- **Cross-Application & Cross-Module Communication** – Provides a unified interface for seamless data exchange.  
- **Connected Graph of Caches & Processors** – Forms a structured pipeline for data processing.  

## Reference  
[SubscribableCache.java](https://github.com/Andyckp/common/blob/master/dispatch/src/main/java/com/ac/common/subscribeable/SubscribableCache.java)  
[Subscribable Cache Repository](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/subscribeable)  

---

# High-Throughput Threading  

## Overview  
This **high-throughput threading model** optimizes **message processing** by balancing **serial execution** for correctness and **parallelism** for performance.  
The library maintains a `Map<PartitionKey, MessageProcessorExecutor>`, where all instances of `MessageProcessorExecutor` share the same **thread pool**, matching the CPU count.  

## Features  
- **Shared Processing Power** – All calculations are executed by the same thread pool.  
- **Serial Execution** – Ensures ordered processing for the same `PartitionKey` to maintain correctness.  
- **Parallel Execution** – Allows concurrent processing across different `PartitionKey` values for improved performance.  
- **Conflation** – Merges redundant messages to reduce workload.  
- **Grace Period** – Prevents flooding and starvation by introducing controlled delays.  

## Reference  
[MessageProcessExecutor.java](https://github.com/Andyckp/common/blob/master/dispatch/src/main/java/com/ac/common/dispatch/MessageProcessExecutor.java)  
[Dispatch Repository](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/dispatch)  

---

# Low-Latency Threading (Experimental)  

## Overview  
This component is **experimental** and explores **Disruptor** usage and **low-GC** message passing across threads.  

## Features  
- **Disruptor** – Investigates the use of Disruptor for high-speed message passing.  
- **Low GC** – Avoids object creation during message passing to minimize garbage collection overhead.  

## Reference  
[Disruptor Repository](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/disruptor)  
[Dispatch2 Repository](https://github.com/Andyckp/common/tree/master/dispatch/src/main/java/com/ac/common/dispatch2)  

---

# Aeron & SBE  

## Overview  
This component explores the use of **Aeron** for high-performance transport and **SBE** for efficient binary encoding.  

## Reference  
[Aeron SBE Repository](https://github.com/Andyckp/common/tree/master/aeron/src/main/java/com/ac/common/sbe)  
[Aeron Test Repository](https://github.com/Andyckp/common/tree/master/aeron/src/test/java/com/ac/common/aeron)  

