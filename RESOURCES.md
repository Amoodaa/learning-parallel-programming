# Parallel & Distributed Programming Resources

## Knowledge

- `source-materials/` (lecture slides): **the syllabus**. Everything taught comes from here. Lecture_1_MSCC_PDP_Introduction.pptx (95 slides), ElementaryParallelism.pptx (100 slides). Slide-by-slide map and errata: reference/course-map.html.
- [Oracle Java Tutorial: Concurrency trail](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
  Short official pages covering almost exactly the slides' Java topics (threads, sleep, join, sync, deadlock, executors, pools, fork/join). Use for: the explanations the slides leave out. Primary reading for most lessons.
- [Java SE 21 API Javadoc: java.lang.Thread](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html), [Thread.State](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.State.html), [RecursiveAction](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/RecursiveAction.html)
  Use for: settling exact method behaviour and correcting slide errors.
- Book: Brian Goetz, *Java Concurrency in Practice* (Addison-Wesley, 2006). On the course reading list (Deck 1 s2). Use for: deeper "why" on visibility/volatile and thread pools, only when a slide needs it.

## Wisdom (Communities)

- Classmates and the lecturer (Osama Abushama, per Deck 1 s1): the best source for "is this on the exam?" and for the C half.
- [r/javahelp](https://www.reddit.com/r/javahelp/): for "why does my threaded code do X" questions. (unverified: community quality not checked this session)

## Gaps

- No slides yet for: synchronization/locking, condition variables, semaphores/latches/barriers, C, OpenMP, MPI (listed in Deck 1 s3 course content). Add them to `source-materials/` when released.
- No past exam papers. These would sharpen the theory lessons a lot.
