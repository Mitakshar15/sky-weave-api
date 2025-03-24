import requests
import json
from datetime import datetime

# Base URL for the endpoint
BASE_URL = "http://localhost:8888/v1/product/create"

# Sample DigitalWork template
DIGITAL_WORK_TEMPLATE = {
    "title": "SAMPLE POST {unique_id}",  # Placeholder for unique title
    "description": "sample test description",
    "creator": "ainkai",
    "files": [
        {
            "fileUrl": "https://docs.google.com/document/d/1gWigTZUE3uOOS9zzk9IDScyCXey44_sbdDCSdBgdmhQ/edit?usp=drive_link",
            "fileType": "text/txt",
            "fileSize": 200102
        }
    ],
    "thumbnailUrl": "string",
    "price": 0.00,
    "isFree": True,
    "tags": [
        {
            "id": "d5873261-1970-46ea-a040-da8971ed40c4",
            "name": "docs"
        }
    ],
    "category": {
        "name": "notes",
        "description": "notes",
        "parentCategory": "docs"
    },
    "status": "PUBLISHED",
    "previewImages": [
        "string"
    ],
    "license": "free"
}

def create_digital_work(auth_token, num_requests=5):
    """
    Automates sending POST requests to create DigitalWorks with unique titles.

    :param auth_token: Authorization token for the header
    :param num_requests: Number of sample requests to send (default: 5)
    """
    headers = {
        "Authorization": f"Bearer {auth_token}",
        "Content-Type": "application/json"
    }

    for i in range(1, num_requests + 1):
        # Generate a unique title using timestamp and index
        unique_title = f"SAMPLE POST {datetime.now().strftime('%Y%m%d%H%M%S')}_{i}"
        payload = DIGITAL_WORK_TEMPLATE.copy()
        payload["title"] = unique_title

        # Send POST request
        try:
            response = requests.post(BASE_URL, headers=headers, data=json.dumps(payload))
            response.raise_for_status()  # Raise an exception for bad status codes

            print(f"Successfully created DigitalWork: {unique_title}")
            print(f"Response: {response.json()}")

        except requests.exceptions.HTTPError as http_err:
            print(f"HTTP error occurred for {unique_title}: {http_err}")
            print(f"Response: {response.text}")
        except Exception as err:
            print(f"Error occurred for {unique_title}: {err}")

def main():
    # Get authorization token from user input
    auth_token = input("Please enter your Authorization token: ").strip()

    # Get number of requests from user (default to 5 if invalid input)
    try:
        num_requests = int(input("Enter the number of sample DigitalWorks to create (default 5): ").strip() or 5)
    except ValueError:
        num_requests = 5
        print("Invalid input, defaulting to 5 requests.")

    # Run the automation
    print(f"Starting to create {num_requests} DigitalWorks...")
    create_digital_work(auth_token, num_requests)
    print("Finished!")

if __name__ == "__main__":
    main()