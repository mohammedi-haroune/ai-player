#Versions
The application has two versions:
- **Native:**  which use a native java library (deeplearning4j) to ues the kears model
- **Python Based:** which use the python keras module to use the kears model 

##Why two versions:
in order to use the keras model the native version make use of deeplearning4j and nd4j java libraries,
theses two libraries are two heavy because of the platform dependents dependencies. the consequence is that
the build jar file will exceed the 500 mb when including them :o :p, so we've decided 
to write a simple python script instead and hence the python based version.
   
# Requirements
## For the native version
- jdk
- sbt

## For the python based version
- python
- tensorflow
- keras
- numpy


#How to use
##Build an executable jar from source
1. clone the repo `git clone`
2. build `sbt assembly`

##Using intellij idea IDE
1. clone the repo
2. import the project as an sbt project
3. run

##Using sbt

