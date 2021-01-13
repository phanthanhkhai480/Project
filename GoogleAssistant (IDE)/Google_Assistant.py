import pyttsx3
import speech_recognition
import webbrowser
from datetime import date, datetime

def main():
    #initialize text-to-speech library and function
    #set rate (per min) and voices id (0 for male, 1 for female)
    speak_ = pyttsx3.init()
    rate_ = speak_.getProperty('rate') 
    speak_.setProperty('rate', 175)
    voices_ = speak_.getProperty('voices')
    speak_.setProperty('voice', voices_[1].id)
    speak_.say("I am ready to listen...")
    speak_.runAndWait()
    repeat_ = 0

    #initialize speech-to-text library and function
    listen_ = speech_recognition.Recognizer()
    #default user name
    userName_ = "Khai"
    while True:
        #initialize the mic ready to listen
        with speech_recognition.Microphone() as mic:
            listener = listen_.listen(mic)

        try:
            first_ = listen_.recognize_google(listener)
        except: 
            first_ = ""
    
        #default response when nothing is heard
        say_ = "I cannot understand, please try again"
        if first_ == "":
            #if there is nothing to say, keep listening
            say_ = ""
            repeat_ += 1
            #if there is nothing to listen 3 times in a row, exit
            if repeat_ == 3:
                break
        
        #command response for hello
        elif "hello" in first_ or "hi" in first_:
            repeat_ = 0
            say_ = "hello " + userName_
        
        #command response for how are you
        elif "how are you" in first_:
            repeat_ = 0
            say_ = "i am fine thank you"
        
        #command response for day asking
        elif "date" in first_ or "day" in first_:
            repeat_ = 0
            date_ = date.today()
            say_ = "today is " + date_.strftime("%B %d, %Y")
        
        #command response for time asking
        elif "time" in first_:
            repeat_ = 0
            time_ = datetime.now()
            say_ = "right now is " + time_.strftime("%I %M %p")
        
        #open a website
        elif "open" in first_:
            repeat_ = 0
            if "Google" in first_:
                url = 'https://google.com/'
                say_ = "open Google"
            elif "Wikipedia" in first_:
                url = 'https://wikipedia.com/'
                say_ = "open Wikipedia"
            elif "Yahoo" in first_:
                url = 'https://yahoo.com/'
                say_ = "open Yahoo"
            elif "YouTube" in first_:
                url = 'https://youtube.com/'
                say_ = "open YouTube"

            #set browser path, if not set, default one will be used
            #chrome_path = "C:/Program Files/Google/Chrome/Application/chrome.exe %s"
            #webbrowser.get(chrome_path).open(url)
            webbrowser.open_new(url)
        
        #search something online
        elif "search" in first_:
            repeat_ = 0
            temp_ = word_search(first_)
            if "Google" in first_:
                url = 'https://www.google.com/search?q=' + temp_
                say_ = "searching " + temp_ + "on Google"
            elif "YouTube" in first_:
                url = 'https://www.youtube.com/results?search_query=' + temp_
                say_ = "searching " + temp_ + "on Youtube"
            else:
                say_ = "Sorry, I can only search result on Google or Youtube only."
            
            #set browser path, if not set, default one will be used
            #chrome_path = "C:/Program Files/Google/Chrome/Application/chrome.exe %s"
            #webbrowser.get(chrome_path).open(url)
            webbrowser.open_new(url)
            
            
        elif "quit" in first_ or "bye" in first_ or "exit" in first_ or "goodbye" in first_:
            speak_.say("goodbye")
            speak_.runAndWait()
            break

        #print("Listen: " + first_)
        #print("Response: " + say_)
        speak_.say(say_)
        speak_.runAndWait()
        

def word_search(pathURL_):
    wordTemp_ = ""

    temp_ = pathURL_.lower().split()

    #right now, it has to start with a word search or find
    if temp_[0] == "search" or temp_[0] == "find":
        #if pattern is search + Youtube/Google + for + something
        if temp_[1] == "youtube" or temp_[1] == "google":
            if temp_[2] == "for":
                for x in range (3, len(temp_)):
                    wordTemp_ += temp_[x]
                    wordTemp_ += " "
        
        #if pattern is search + something + on + Youtube/Google
        else:
            if temp_[len(temp_)-2] == "on":
                if temp_[len(temp_)-1] == "youtube" or temp_[len(temp_)-1] == "google":
                    for x in range (1, (len(temp_)-2)):
                        wordTemp_ += temp_[x]
                        wordTemp_ += " "
    return wordTemp_


if __name__ == "__main__":
    main()
