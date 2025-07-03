Performance:
- use int instead of LocalDate internally
- reuse char[] from charArray
- use padding to avoid false sharing
- data partitioning *

Implementation
- implement delta gamma adjustment
- add sequence number in SBE schema
- subscribe to capture stream after replay stream finishes
- have two separate aeron managers for main and test

Documentation
- add tests / logs
- update readme.md **
