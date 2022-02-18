# Spectrophotometry

## About
This is an application designed to control an ESP32 based mini-spectrophotometer, process and visualize the
collected data.

### What is [Spectrophotometry](https://en.wikipedia.org/wiki/Spectrophotometry)?
Spectrophotometry is a branch of spectroscopy used for the quantitative analysis of molecules.
The analysis is based on the amount of light absorbed by colored compounds.
Measurements are carried out with a homemade, wireless Bluetooth device built to analyze the
light absorption of liquid samples in the visible range.
The results are stored and visualized, and the application is capable of determining unknown concentrations 
as well.

![device](doc/device.jpg)

### Main features
- Bluetooth Low Energy connection to selected device / autoconnect
- analyze transmitted light / absorbance spectra
- measure absorbance on selected wavelength / spectral maximum
- calculate concentration from selected samples (using a calibration equation)
- store experiment results in a local database instance
- view selected experiments / spectra
- multiple configuration options on the Settings page
- export results to JSON format

### References
[Laganovska et al.](https://www.sciencedirect.com/science/article/pii/S246806722030016X) created an open source spectrophotometer with advanced features in 2020,
check this out if you are interested in the topic!