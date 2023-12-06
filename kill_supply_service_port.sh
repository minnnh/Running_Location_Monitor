#!/bin/bash
# remember to `chmod +x filename.sh` first

# supply-location-service : 9001

# List all processes using specified ports
PORTS=(9001)  # Add your ports here
for port in "${PORTS[@]}"; do
    echo "Processes using port $port:"
    lsof -i :$port
done

# Kill all processes using specified ports
for port in "${PORTS[@]}"; do
    echo "Killing processes using port $port:"
    lsof -ti :$port | xargs kill -9
done


