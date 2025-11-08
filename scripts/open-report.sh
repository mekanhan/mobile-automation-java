#!/bin/bash
# Simple script to open Allure report with a local HTTP server

cd target/allure-report

# Check if report exists
if [ ! -f "index.html" ]; then
    echo "Error: Report not found. Run 'mvn allure:report' first."
    exit 1
fi

echo "Starting HTTP server on http://localhost:8765"
echo "Opening report in browser..."
echo "Press Ctrl+C to stop the server"

# Start Python HTTP server and open browser
python3 -m http.server 8765 &
SERVER_PID=$!

sleep 2
open http://localhost:8765

# Wait for user to press Ctrl+C
trap "kill $SERVER_PID; exit" INT
wait $SERVER_PID
