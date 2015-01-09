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
  (> (count (db/username-exists? username)) 1))

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
      :else (redirect "/home")
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

(defroutes login-routes
  (GET "/" [] (login))
  (GET "/login" [] (login))
  (POST "/login" [username password] (login-user username password))
  (GET "/register" [] (register))
  (POST "/register" [first-name last-name email username password repeat-password] (register-user first-name last-name email username password repeat-password)
        ))

