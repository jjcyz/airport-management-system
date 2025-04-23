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
    javac -cp json-20231013.jar -d .dist src/main/ui/Main.java src/main/ui/*.java src/main/model/*.java src/main/persistence/*.java
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Build successful!${NC}"
    else
        echo -e "${RED}Build failed!${NC}"
        exit 1
    fi
}

# Function to run the application
run() {
    echo -e "${GREEN}Running application...${NC}"
    java -cp .dist:json-20231013.jar ui.Main
}

# Function to clean build artifacts
clean() {
    echo -e "${GREEN}Cleaning build artifacts...${NC}"
    rm -rf .dist
    echo -e "${GREEN}Clean complete!${NC}"
}

# Main script logic
case "$1" in
    "build")
        build
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
