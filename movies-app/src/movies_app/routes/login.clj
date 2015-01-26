(ns movies-app.routes.login
  (:require [compojure.core :refer [defroutes GET POST]]
            [movies-app.views.layout :as layout]
            [hiccup.form :refer [form-to label text-field radio-button password-field submit-button email-field]]
            [hiccup.element :refer :all]
            [hiccup.core :refer :all]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.request :as request]
            [noir.validation :as validator]
            [movies-app.models.database :as db]
            [clojure.string :as str])
  (:import (org.bson.types ObjectId)))

(defn login []
  "Login form"
  (layout/common 
    
    [:div "Enter login credentials "
     ]
    (form-to [:post "/login"]     
             (html 
               [:div.login
                (text-field {:placeholder "username"} :username) 
                [:br]
                (password-field {:placeholder "password"} :password )
                [:br]
                
                (submit-button "Login")
                [:br]
                [:br]
                [:div.error (session/flash-get :error-message)]
                (link-to "/register" "Register")
                ]))))

(defn edit-profile []
  (let 
    
    [user (db/get-user (str(session/get :username)))]
  
    (layout/common
      [:div.header 
       [:p "Update profile"
        ]]
      (form-to [:post "/update"]
               (label :first-name "First name ")
               (text-field {:value (:first-name user)} :first-name)
               (label :last-name "Last name ")
               (text-field {:value (:last-name user)} :last-name)
               (label :email "Email ")
               (text-field {:value (:email user)} :email) 
               (label :username "Username ")
               (text-field {:value (:username user)} :username) 
               (label :password "Password ")
               (text-field {:value (:password user)} :password )
               
               [:br]
               (submit-button "Save")
               [:br]
               [:br]
               [:div.error (session/flash-get :success-message)]
               ))
    
    ))


(defn register []
  "Register form"
  (layout/common
    [:div.header 
     [:div "Registration "
      ]]
    (form-to [:post "/register"]              
             (text-field {:placeholder "First name"} :first-name)
             (text-field {:placeholder "Last name"} :last-name)
             (text-field {:placeholder "email"} :email) 
             (text-field {:placeholder "username"} :username) 
             (password-field {:placeholder "password"} :password )
             (password-field {:placeholder "repeat password"} :repeat-password )
             [:br]
             (submit-button "Register")
             [:br]
             [:br]
             [:div.error (session/flash-get :error-message)]
             )))

(defn number-of-users [username]
  (= (count (db/username-exists? username)) 1))

(defn validate-registration [first-name last-name email username password]
  (validator/has-values? [first-name last-name email username password]))

(defn validate-login [username password]
  (validator/has-values? [username password]
                         ))

(defn login-user [username password]  
  (let [errors (validate-login  username password) ]
    (cond 
      (= false errors)
      (do
        (session/flash-put! :error-message "Enter values.")
        (redirect "/"))
      (= false (number-of-users username)) 
      (do
        (session/flash-put! :error-message "Username does not exist.")
        (redirect "/"))
      (= nil (db/validate-password username password))
      (do
        (session/flash-put! :error-message "Wrong password.")
        (redirect "/"))
      :else 
      (do 
      
        (session/put! :username username)
       
        (redirect "/home"))
      )))

(defn register-user [first-name last-name email username password repeat-password]
  (let [errors (validate-registration first-name last-name email username password) ]
    (cond (= false errors)
          (do
            (session/flash-put! :error-message "All fields are mandatory.")
            (redirect "/register"))
          (> 5 (.length username))
          (do
            (session/flash-put! :error-message "Username must have at least five characters.")
            (redirect "/register"))
          (> 5 (.length password))
          (do
            (session/flash-put! :error-message "Password must have at least five characters.")
            (redirect "/register"))
          (= false (validator/is-email? email))
          (do
            (session/flash-put! :error-message "Enter valid email address.")
            (redirect "/register"))
          (= false (.equals password repeat-password))
          (do
            (session/flash-put! :error-message "Passwords do not match.")
            (redirect "/register"))
          (= false (number-of-users username))
          (do
            (session/flash-put! :error-message "Username already exists.")
            (redirect "/register"))
          :else (do 
                  (db/create-user first-name last-name email username password)
                  (redirect "/"))
          )))

(defn update-user [user]
  (print (str (session/get :username)))
  (let [existing-user (db/get-user (str (session/get :username)))
        new-user (merge existing-user user)]
    (if-not (.equals (str (session/get :username)) (:username new-user))
       
        (session/put! :username (:username new-user))
      )
    (db/update-user existing-user new-user)    
     
    (session/flash-put! :success-message "Profile successfully updated.")
    (redirect "/edit-profile")
    
    )
;      (println (db/update-user))
  )

(defroutes login-routes
  (GET "/" [] (login))
  (GET "/login" [] (login))
  (POST "/login" [username password] (login-user username password))
  (GET "/register" [] (register))
  (GET "/edit-profile" [] (edit-profile))
  (POST "/update" [first-name last-name email username password] (update-user {:first-name first-name :last-name last-name :email email :username username :password password}))
  (POST "/register" [first-name last-name email username password repeat-password] (register-user first-name last-name email username password repeat-password)
        ))

