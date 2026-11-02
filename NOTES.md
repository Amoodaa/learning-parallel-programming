# Teaching Notes

- Learner wants the **minimum necessary**: stay strictly inside `source-materials/`. Use external sources only to explain or verify slides.
- Main pain points: slide code is unexplained, theory is vague, and the slides are meant to be talked over. Consecutive slides feel unrelated. **Always say which slides a lesson covers and how they connect** (course-map.html is the anchor).
- Java is rusty: briefly explain syntax that matters (e.g. `throws InterruptedException`, generics in `Future<Integer>`).
- Goal is both assignment and exam, equally: mix coding exercises and theory recall.
- Runs Java 26 locally; single-file `java Foo.java` works, so exercises are self-checking single files printing PASS/FAIL.
- Slides contain bugs; point them out instead of letting them confuse. Errata table lives in reference/course-map.html.
- Lesson plan (19 lessons: Deck 1 → 1–3, Deck 2 → 4–7, Deck 3 → 8–11, Deck 4 Condition variables → 12–14, Deck 5 Semaphores → 15–16, Deck 6 Latches/barriers → 17–18, Deck 7 Lock-free → 19) is in course-map.html. Update it when new slides arrive.
- Decks 4–7 are old binary .ppt with no LibreOffice on this machine; text was extracted with olefile (record-level parse of the PowerPoint Document stream, slides ordered via the persist directory). The JDK is at ~/.jdks/openjdk-27 (not on PATH).
- Exercise checkers must never hang on a wrong answer: use daemon threads with one overall join deadline, and stop a repeated part at its first stuck run.
- Prefers simple typography: system sans-serif and system monospace, no web fonts, no italic or small-caps styling.
- Code blocks get syntax highlighting via assets/highlight.js (highlight.js 11.11.1 from cdnjs; Java/C/bash bundled). Keep code lines short enough to avoid horizontal scroll (~60 chars).
- Theory lessons (e.g. 0003) swap the PASS/FAIL Java exercise for exam-style written answers with model answers in collapsed recall cards; a runnable demo (Race.java) may illustrate, without a fix that belongs to a later lecture.
- Each lesson ends with "Next time, from memory" cards that include at least one from an earlier lesson (spacing).
- Timing-based lessons (6, 7, 10) quote measurements from this machine (16 cores, Java 26, pools of 2 threads to mirror the slides' dual-core); say "on my machine" and re-measure if the exercise changes.
