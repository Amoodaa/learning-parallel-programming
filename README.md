# Parallel & Distributed Programming

Short HTML lessons, single-file Java exercises, and reference notes based on the lecture decks in `source-materials/`.

Open `index.html` to start. The course map lists the eleven lessons, detailed slide ranges, and slide corrections.

## GitHub Pages

1. In this repository's **Settings → Pages**, choose **GitHub Actions** as the build source.
2. Merge the Pages workflow into `master`. Each push to `master` publishes the site. You can also run **Deploy to GitHub Pages** from the Actions tab.
3. The site will be at <https://amoodaa.github.io/learning-parallel-programming/> after the workflow succeeds.

No build tools or dependencies are required. The workflow publishes only `index.html`, `assets/`, `lessons/`, `reference/`, and the slide decks. Learning records and agent notes are not included. Links are relative so they work under the repository's Pages subpath.

## Local preview

```sh
python3 -m http.server 8000
```

Open `http://localhost:8000` on your own machine. Java exercises are linked from the lessons; download them and run `java File.java` locally.
