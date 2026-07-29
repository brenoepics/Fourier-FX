# FourierFX

FourierFX is a [JavaFX](https://openjfx.io/) application that visualizes [Fourier series](https://math.mit.edu/~gs/cse/websections/cse41.pdf).
It allows users to select different waveforms and adjust the frequency and order of the Fourier series.

It also runs in the browser: the same Java core is compiled to [WebAssembly](https://webassembly.org/) with [TeaVM](https://teavm.org/).

## Try it online

▶ **[brenoepics.github.io/Fourier-FX](https://brenoepics.github.io/Fourier-FX/)** — Java compiled to WebAssembly, no plugins needed
(requires a browser with WebAssembly GC: Chrome 119+, Firefox 121+, Safari 18.4+).

## Features

- Selection of different waveforms: Square, Sine, Sawtooth, Triangle
- Adjustable frequency and order of the Fourier series
- Scale adjustment
- Debug mode to visualize the individual terms of the Fourier series
- Runs as a native desktop app (JavaFX) and in the browser (WebAssembly)

## Preview

![preview](https://github.com/brenoepics/Fourier-FX/assets/59066707/33a50bfd-992f-4a47-a03c-cf8bac189d30)

## Project structure

| Module  | Description                                                              |
|---------|--------------------------------------------------------------------------|
| `core`  | Pure-Java Fourier series engine, no UI dependencies                      |
| `app`   | JavaFX desktop application                                               |
| `web`   | Browser app: `core` + a thin canvas renderer compiled to WASM with TeaVM |

## Requirements

- Java 25 (toolchain, resolved automatically by Gradle)
- JavaFX 26
- Gradle 9 (via the wrapper)

## Pre compiled

Download from [releases](https://github.com/brenoepics/Fourier-FX/releases)

Install and open program fourier-fx.

## Building

Desktop installer:

```bash
./gradlew :app:package
```

Web (WebAssembly) bundle, output in `web/build/dist`:

```bash
./gradlew :web:webDist
```

## Running

Desktop app:

```bash
./gradlew :app:run
```

Web app (serve the bundle with any static file server):

```bash
./gradlew :web:webDist && python3 -m http.server 8123 -d web/build/dist
```

The web app is deployed to GitHub Pages automatically on every push to `main`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

If you find this project helpful, please consider giving it a ⭐️.

## License

This project is licensed under the MIT License—see the [LICENSE](LICENSE) file for details.
