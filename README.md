# SyncPrem Uprising

## Overview

##### SyncPrem Uprising is a *serverless syndication platform*. What exactly does that mean?

A syndication platform has three key capabilities:

* Produce a stream of records from a source and consume into a destination, similar to an enterprise application integration system.
* Titrate streams of records in a stateless and ephemeral manner.
* Process streams of records as they appear from the source.

Uprising is generally used for two broad classes of solutions:

* Building unbuffered, streaming I/O oriented data pipelines that efficiently promotes data between disparate hosting zones.
* Building self-hosted, embedded pipeline components that transform or react to streams of data.

To understand how Uprising does these things, let's dive in and explore Uprising's capabilities from the bottom up.

First a few concepts:

* Uprising is run as a pipeline in the form of a task on a single compute container.
* The Uprising pipeline bounds a stream of records within a channel.
* Each record consists in totality of an index, topic, schema, payload, instant, partition, offset, and headers.

Uprising has an abstraction API which consists of the following extensibility points:

* Connectors - enables building and running reusable source and/or destination adapters connecting a variety of systems and services using pull-iterator semantics.
* Processors - enables a pipeline to act as a stream processor, applying transformation logic in a deferred-execution manner.
* Runtime - enables advanced customization of the core platform plumbing.
* Configuration - establishes a rich in-memory and over-the-wire configuration model.

In Uprising, there is no concept of communication between any 'clients' or 'servers' as the grain of distribution is more "compact".

Distributed streaming platforms are designed to "widen naturally" at cloud scale - this is due to their distributed, horizontally elastic architecture.
Centralized integration platforms are typically designed to "mushroom awkwardly" at enterprise scale - this is due to their centralized, vertically constrained architecture with horizontal elasticity as an afterthought.
While these approaches have their place in a variety of solutions, Uprising was designed to be intentionally different - while using familiar software and distributed systems patterns.

To use an analogy:

*IF*

* Kafka cloud-scale elasticity is to the *observable universe* in classical physics

*THEN*

* Uprising compute-scale resiliency is to the *bouncy castle* in quantum physics. 

##### Putting the Pieces Together

Uprising is designed to operate at commodity appliance scale (e.g. Raspberry Pi) with full fidelity over cloud-native serverless compute containers (e.g. AWS Lambda). 
Uprising, with its unique architecture, makes it well-suited for on-premises to cloud data synchronization use cases where IT friction is contraindicated (e.g. 'cloud-to-ground' branch office topologies).

##### What's the Catch?

There is none. Uprising is a fully functional, extensible, open source software project and is also an integral component of the overall SyncPrem commercial ecosystem.

# Development

To build a development snapshot of Uprising, you can use either:

* IntelliJ or your favorite Java IDE
* Maven via standard lifecycle phases

# FAQ

Refer frequently asked questions on SyncPrem Uprising here:

* Wiki: https://github.com/syncprem/uprising/wiki/FAQ

# Contribute

- Source Code: https://github.com/syncprem/uprising
- Issue Tracker: https://github.com/syncprem/uprising/issues

# License

The project is licensed under the MIT License.
