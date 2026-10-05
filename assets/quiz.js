// Multiple-choice quiz component. Markup:
//
// <div class="quiz">
//   <p class="q">Question text</p>
//   <div class="options">
//     <button class="opt" data-correct>right answer</button>
//     <button class="opt">wrong answer</button>
//   </div>
//   <div class="why">Explanation shown after answering.</div>
// </div>
//
// Options are shuffled on load so position gives nothing away. One attempt
// per question; the first click counts (retrieval, not trial and error).
// A <p class="score"></p> anywhere on the page shows the running score.

(function () {
  function shuffle(el) {
    const kids = Array.from(el.children);
    for (let i = kids.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [kids[i], kids[j]] = [kids[j], kids[i]];
    }
    kids.forEach((k) => el.appendChild(k));
  }

  function init() {
    const quizzes = Array.from(document.querySelectorAll(".quiz"));
    const scoreEl = document.querySelector(".score");
    let answered = 0, correct = 0;

    function updateScore() {
      if (!scoreEl) return;
      scoreEl.textContent = answered === quizzes.length
        ? `You got ${correct} of ${quizzes.length} on the first try.` +
          (correct < quizzes.length ? " Reread the explanations for the ones you missed, then ask about anything that still seems odd." : "")
        : `${answered} of ${quizzes.length} answered.`;
    }

    quizzes.forEach((quiz, idx) => {
      const q = quiz.querySelector(".q");
      if (q && !q.querySelector(".q-num")) {
        const n = document.createElement("span");
        n.className = "q-num";
        n.textContent = `${idx + 1}.`;
        q.prepend(n);
      }
      const opts = quiz.querySelector(".options");
      shuffle(opts);
      const why = quiz.querySelector(".why");
      opts.querySelectorAll("button.opt").forEach((btn) => {
        btn.addEventListener("click", () => {
          if (quiz.classList.contains("done")) return;
          quiz.classList.add("done");
          const isRight = btn.hasAttribute("data-correct");
          answered++; if (isRight) correct++;
          opts.querySelectorAll("button.opt").forEach((b) => {
            b.disabled = true;
            if (b.hasAttribute("data-correct")) b.classList.add("right");
          });
          if (!isRight) btn.classList.add("wrong");
          if (why) {
            const v = document.createElement("span");
            v.className = "verdict " + (isRight ? "right" : "wrong");
            v.textContent = isRight ? "Correct. " : "Not quite. ";
            why.prepend(v);
          }
          updateScore();
        });
      });
    });
    updateScore();
  }

  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", init);
  else init();
})();
