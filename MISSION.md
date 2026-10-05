# Mission: Parallel & Distributed Programming (MSCC-PDP, uni module)

## Why
Pass the module well: the Java and C assignments (50%) and the theory + coding exam (50%) carry equal weight. The lecture slides in `source-materials/` are written to be talked over, so they don't work for self-study. The goal is to understand them well enough to do both assignments and the exam without learning more than the module needs.

## Success looks like
- Can read any slide in `source-materials/` and explain what it is for and how it connects to the slides around it.
- Can write, from scratch, Java code that splits work across threads (split → start → join → combine), with a thread pool, and with Fork/Join.
- Can answer exam theory questions: concurrent vs parallel, critical-section conditions, deadlock conditions and strategies, why more threads can be slower.
- Can spot the bugs in slide code (see the errata in reference/course-map.html).

## Constraints
- Stick to `source-materials/`. External sources are used only to explain or verify slide content, never to add new topics.
- Java is rusty: explain syntax when it matters, but don't teach Java from scratch.
- No known deadline yet; lessons should be short.

## Out of scope
- Anything not in the slides (virtual threads, CompletableFuture, streams, java.util.concurrent beyond what the slides use).
- The C/OpenMP/MPI half, until its slides are added to `source-materials/`.
