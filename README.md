## healthcard-java

With this java library you can simply read patient and insurance data from German public health insurance cards. (Elektronische Gesundheitskarte eGK)

It is based off of this repo: https://github.com/waigel/healthcard-rust

## Installation

```gradle
implementation("com.waigel.healthcard:healthcard-java")
```

## Example

```kotlin
val terminalFactory = TerminalFactory.getDefault()
val reader = HealthCardReader(terminal.terminals().list()[0])
println("Reader: ${reader.getCardTerminal()}")

class Listener() : HealthCardReaderEvents {
    override fun onCardInserted() {
        println("Card inserted")
    }
    override fun onCardRemoved() {
        println("Card removed")
    }
    override fun onCardReadError(e: Exception) {
       println("Card read error: $e")
    }
    override fun onCardReadDataSuccessfully(
        patient: PatientData,
        insurance: InsuranceData,
        generation: HealthCardGeneration
    ) {
        println("Card read successfully")
        println("Patient: $patient")
        println("Insurance: $insurance")
        println("Generation: $generation")
    }
}
reader.addEventListener(Listener())
```

## Configuration

You need libpcsclite installed on your computer. Often dthis package is shipped with the os per default.

**Ubuntu 18.0**
On ubuntu pcsc is in the wrong folder for the java smard card reader, so no terminals would be found. 
You can change this path with a java runtime argument. For example: `-Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1`
Use the command to find out your libpcsclite location: `ldd -r /usr/bin/pcsc_scan`

___
This project is licensed under MIT.
