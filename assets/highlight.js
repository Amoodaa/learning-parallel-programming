// Syntax highlighting for every <pre> block, using highlight.js from cdnjs.
// Include with: <script src="../assets/highlight.js" defer></script>
// Java, C and bash are in the core bundle. Blocks are treated as Java unless they carry a class like "language-bash".
// Token colours live in style.css so they follow the light/dark theme.

(function () {
  const base = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.11.1/";

  function load(src) {
    return new Promise((resolve, reject) => {
      const s = document.createElement("script");
      s.src = src; s.onload = resolve; s.onerror = reject;
      document.head.appendChild(s);
    });
  }

  function run() {
    load(base + "highlight.min.js")
      .then(() => {
        document.querySelectorAll("pre").forEach((pre) => {
          if (!/language-/.test(pre.className)) pre.classList.add("language-java");
          window.hljs.highlightElement(pre);
        });
      })
      .catch(() => { /* offline: plain monospace is fine */ });
  }

  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", run);
  else run();
})();
