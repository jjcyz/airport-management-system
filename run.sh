#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to display help
show_help() {
    echo "Airport Management System - Build and Run Script"
    echo "Usage: ./run.sh [command]"
    echo ""
    echo "Commands:"
    echo "  build    - Compile the project"
    echo "  test     - Run tests"
    echo "  test-compile - Compile and run specific test compilation"
    echo "  run      - Run the application"
    echo "  clean    - Clean build artifacts"
    echo "  setup    - Setup initial configuration"
    echo "  all      - Build and run the application"
}

# Function to setup initial configuration
setup() {
    echo -e "${GREEN}Setting up initial configuration...${NC}"
    mkdir -p data
    echo '{"listOfPassengers":[],"listOfAircraft":[],"listOfFlights":[]}' > data/airport.json
    echo -e "${GREEN}Setup complete!${NC}"
}

# Function to build the project
build() {
    echo -e "${GREEN}Building project...${NC}"
    ./gradlew build
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Build successful!${NC}"
    else
        echo -e "${RED}Build failed!${NC}"
        exit 1
    fi
}

# Function to run tests
test() {
    echo -e "${GREEN}Running tests...${NC}"
    ./gradlew test
}

# Function to run the application
run() {
    echo -e "${GREEN}Running application...${NC}"
    java -cp build/classes/java/main:build/libs/* ui.Main
}

# Function to clean build artifacts
clean() {
    echo -e "${GREEN}Cleaning build artifacts...${NC}"
    ./gradlew clean
    echo -e "${GREEN}Clean complete!${NC}"
}

# Function to run specific test compilation
test_compile() {
    echo -e "${GREEN}Compiling and running specific test compilation...${NC}"
    mkdir -p .dist
    javac -cp json-20231013.jar -d .dist src/main/ui/Main.java src/main/ui/*.java src/main/model/*.java src/main/persistence/*.java src/test/model/TestRunner.java
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Compilation successful!${NC}"
        java -cp .dist:json-20231013.jar model.TestRunner
    else
        echo -e "${RED}Compilation failed!${NC}"
        exit 1
    fi
}

# Main script logic
case "$1" in
    "build")
        build
        ;;
    "test")
        test
        ;;
    "test-compile")
        test_compile
        ;;
    "run")
        run
        ;;
    "clean")
        clean
        ;;
    "setup")
        setup
        ;;
    "all")
        build
        run
        ;;
    *)
        show_help
        ;;
esac
