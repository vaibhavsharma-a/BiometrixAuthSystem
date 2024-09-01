from flask import Flask, render_template, request, jsonify, redirect, url_for
import face_recognition
import json
import base64
from io import BytesIO
from PIL import Image
import numpy as np
import tkinter as tk
import firebase_admin
from firebase_admin import messaging

# Replace with the path to your downloaded Google Services JSON file
cred = firebase_admin.credentials.Certificate('C:/Users/harsh/Desktop/FaceRek/bioauth-49b70-firebase-adminsdk-h8h1q-955bf71f06.json')
firebase_admin.initialize_app(cred)

# Loading user data from JSON file
def load_users():
    with open('users.json') as f:
        return json.load(f)

app = Flask(__name__)


users = load_users()

# Function to hash passwords
def hash_password(password):
    # keeping it simple, could change later
    return f"hashed_{password}"

#default landing page
@app.route('/')
def index():
    return render_template("welcome.html")

@app.route('/send_notification', methods=['POST'])
def send_notification():
    
    data = request.get_json()
    if data is None:
        return "No data received", 400  # Bad request

    username = data.get('username')
    # username = request.form['username']
    # Logic to fetch user details and registration token based on username
    user_data = get_user_token(username)

    print(username)

    if user_data:
        try:
            message = messaging.Message(
                notification=messaging.Notification(
                    title='Notification from Flask App',
                    body='This is a test notification'
                ),
                token=user_data.get('registration_token')
            )
            response = messaging.send(message)
            print(f'Notification sent successfully: {response}')
            return 'Notification sent successfully!'
        except Exception as e:
            print(f'Error sending notification: {e}')
            return f'Error sending notification: {e}'
    else:
        return 'User not found or registration token unavailable.'
    
def get_user_token(username):
    
    user_data = {'username': 'abc', 'registration_token': 'YOUR_TOKEN_HERE'}
    return user_data if user_data['username'] == username else None  # Match username

fcm_tokens = {}

@app.route('/store_fcm_token', methods=['POST'])
def store_fcm_token():
    data = request.get_json()
    if data is None:
        return "No data received", 400  # Bad request

    username = data.get('username')
    fcm_token = data.get('fcm_token')  # Assuming this is how the token is sent from the client

    if username and fcm_token:
        # Store the FCM token in your database along with the username
        # Example: Update the user record in your database with the FCM token
        # This code will depend on your specific database setup and ORM (if used)
        # Example using SQLAlchemy:
        # user = User.query.filter_by(username=username).first()
        # if user:
        #     user.fcm_token = fcm_token
        #     db.session.commit()
        fcm_tokens[username] = fcm_token
        return 'FCM token stored successfully!'
    else:
        return 'Incomplete data received', 400


# Route for user registration
@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        return render_template("register.html")

    else:
        username = request.form['username']
        password = request.form['password']
        image = request.files['image']

        if image is None:
            image = request.files['captured_image']

        face_encoding = face_recognition.face_encodings(face_recognition.load_image_file(image))[0]

        # Add new user data to the list
        users.append({
            "username": username,
            "password": hash_password(password),
            "face_encoding": face_encoding.tolist()  # Convert encoding to list for JSON
        })

        # Save updated user data to JSON file
        with open('users.json', 'w') as f:
            json.dump(users, f)

        return jsonify({'success': True, 'message': 'Registration successful!'})

        
@app.route('/dashboard', methods=['GET', 'POST'])
def dashboard():
    return render_template('dashboard.html')

# Route for login attempt
@app.route('/login', methods=['GET', 'POST'])
def login():
    #check the http request method to process request accordingly
    if request.method=='GET':
        return render_template('login.html')
    else:
        username = request.form.get('username')
        password = request.form.get('password')
        image_data_base64 = request.form.get('image')

    # Decode base64-encoded image data
        image_data = base64.b64decode(image_data_base64.split(',')[1])

        # Create PIL Image object from the decoded image data
        image_pil = Image.open(BytesIO(image_data))

        # Convert PIL Image to numpy array
        img_np = np.array(image_pil)

        # Detect faces in the uploaded image
        face_locations = face_recognition.face_locations(img_np)
        if not face_locations:
            return jsonify({'success': False, 'message': 'No face detected!'})

        # Extract face encodings from the detected faces
        face_encodings = face_recognition.face_encodings(img_np, face_locations)

        # Load user data
        user_data = load_users()
            
        for user in user_data:
            
            user_face_encoding = np.array(user['face_encoding'])
            match = face_recognition.compare_faces([user_face_encoding], face_encodings[0])
            if any(match):
                print(user['username'])
                return redirect(url_for('dashboard'))
                # return jsonify({'success': True, 'message': f"Welcome, {user['username']}!"})
            
        return jsonify({'fail': False, 'message': 'Face authentication failed!'})


if __name__ == '__main__':
    app.run(debug=True)
    app.run(host='0.0.0.0', port=8080)
