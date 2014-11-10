*heights-data-scanner* - tool for discovering steep downhills on a flatland

**Features:**

- Output scan results in a KML format for easy browsing
- Auto-download of data specified by coordinate range
- Export to CSV

**Download Release Version**

https://github.com/sshymov/heights-data-scanner/releases/download/1.0/heightsMapScanner-1.0.zip

**Building:**

mvn clean install

**Usage:**

    java CmdLineApp [options...] <outputfile>
    -format [KML | CSV]  : [optional] Output format can KML or CSV, KML is default
    -lat VAL             : Latitude value, e.g.: 47 or 45-47
    -lon VAL             : Longitude value, e.g.: 30 or 29-33
    -min-avr-steepness N : Minimal average slope in degrees from horizontal, e.g.
                        20
    -min-height N        : Minimal height of the hill in meters
    -min-max-steepness N : [optional] Minimal maximal slope of a segment in
                        degrees from horizontal, e.g. 20


**Usage Example:**

heightsMapScanner/bin/heightsMapScanner -lat 45-48 -lon 29-31 -min-avr-steepness 15 -min-height 45 -min-max-steepness 17 output.kml


2014 (C) Stanislav Shymov
shtas{at}yandex{dot}ua
