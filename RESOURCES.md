# Parallel & Distributed Programming Resources

## Knowledge

- `source-materials/` (lecture slides): **the syllabus**. Everything taught comes from here. Lecture_1_MSCC_PDP_Introduction.pptx (95 slides), ElementaryParallelism.pptx (100 slides), SharingResources.ppt (101 slides), ConditionVariablesLecture.ppt (56), Semaphores.ppt (45), LatchesBarriers.ppt (53), LockFreeDataStructures.ppt (35). The .ppt files are old binary format: read them via `soffice --headless --convert-to pptx`, or with Python `olefile` if LibreOffice isn't installed. Slide-by-slide map and errata: reference/course-map.html.
- [Oracle Java Tutorial: Concurrency trail](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
  Short official pages covering almost exactly the slides' Java topics (threads, sleep, join, sync, deadlock, executors, pools, fork/join). Use for: the explanations the slides leave out. Primary reading for most lessons.
- [Java SE 21 API Javadoc: java.lang.Thread](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html), [Thread.State](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.State.html), [RecursiveAction](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/RecursiveAction.html)
  Use for: settling exact method behaviour and correcting slide errors.
- [OSTEP (Arpaci-Dusseau), *Operating Systems: Three Easy Pieces*, ch. 26 Concurrency: An Introduction](https://pages.cs.wisc.edu/~remzi/OSTEP/threads-intro.pdf) and [ch. 32 Common Concurrency Problems](https://pages.cs.wisc.edu/~remzi/OSTEP/threads-bugs.pdf)
  Free, widely used OS textbook; examples in C. Use for: critical sections, race conditions, deadlock conditions/prevention (lesson 3), and later the C half.
- [Java SE 21 Javadoc: Condition](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/Condition.html), [Semaphore](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Semaphore.html), [CountDownLatch](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CountDownLatch.html), [CyclicBarrier](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CyclicBarrier.html), [BlockingQueue](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/BlockingQueue.html)
  Use for: exact behaviour of the Deck 4–6 classes (lessons 12–18). Condition's bounded-buffer example is the corrected Deck 4 buffer.
- [OSTEP ch. 31 Semaphores](https://pages.cs.wisc.edu/~remzi/OSTEP/threads-sema.pdf)
  Use for: semaphores for ordering and producer/consumer, reader-writer locks, dining philosophers (lessons 13, 15, 16).
- [Oracle Java Tutorial: Interrupts](https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html) and [Atomic Variables](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html)
  Use for: lessons 14 and 19.
- Book: Brian Goetz, *Java Concurrency in Practice* (Addison-Wesley, 2006). On the course reading list (Deck 1 s2). Use for: deeper "why" on visibility/volatile and thread pools, only when a slide needs it. Ch. 15 is the source of Deck 7 slides 5–6.

## Wisdom (Communities)

- Classmates and the lecturers (Osama Abushama, per Deck 1 s1; Decks 4–7 are by Tony Mullins): the best source for "is this on the exam?" and for the C half.
- [r/javahelp](https://www.reddit.com/r/javahelp/): for "why does my threaded code do X" questions. (unverified: community quality not checked this session)

## Gaps

- No slides yet for: C, OpenMP, MPI (listed in Deck 1 s3 course content). Add them to `source-materials/` when released.
- No past exam papers. These would sharpen the theory lessons a lot.
