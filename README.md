# FourierFX

FourierFX is a [JavaFX](https://openjfx.io/) application that visualizes [Fourier series](https://math.mit.edu/~gs/cse/websections/cse41.pdf).
It allows users to select different waveforms and adjust the frequency and order of the Fourier series.

## Features

- Selection of different waveforms: Square, Sine, Sawtooth, Triangle
- Adjustable frequency and order of the Fourier series
- Scale adjustment
- Debug mode to visualize the individual terms of the Fourier series

## Preview

> [!NOTE] 
> Using JPro, I could transform the Java canvas to Javascript that runs online but may be down or unstable.
> Check here the [Fourier Preview](https://fourier.breno.tech/)

![preview](https://github.com/brenoepics/Fourier-FX/assets/59066707/33a50bfd-992f-4a47-a03c-cf8bac189d30)

## Requirements

- Java 21
- JavaFX 21
- Gradle 8.5

## Pre compiled

Download from [releases](https://github.com/brenoepics/Fourier-FX/releases)

Install and open program fourier-fx.

## Building

To build the project, use the following Maven command:

```bash
gradlew package
```

## Running

To run the project, use the following Maven command:

```bash
gradlew run
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

If you find this project helpful, please consider giving it a ⭐️.

## License

This project is licensed under the MIT License—see the [LICENSE](LICENSE) file for details.
