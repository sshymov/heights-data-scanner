*heights-data-scanner* - tool for discovering steep downhills on a flatland

**Features:**

- Output scan results in a KML format for easy browsing or PNG image
- Autodownload of data specified by coordinate range

**Building:**

mvn clean install

**Usage:**
    java CmdLineApp [options...] <outputfile>
     -format [KML | PNG] : Output format, KML is default
     -lat VAL            : Latitude value, e.g.: 47 or 45-47
     -lon VAL            : Longitude value, e.g.: 30 or 29-33
     -min-height N       : Minimal height of the hill in meters
     -min-steepness N    : Minimal steepness of a hill in degrees from horizontal,
                           e.g. 30

**Usage Example:**

target/heightsMapScanner/bin/heightsMapScanner -lat 46 -lon 29-32 -min-height 55 -min-steepness 15 output.kml

