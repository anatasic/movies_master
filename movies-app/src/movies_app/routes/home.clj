(ns movies-app.routes.home
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.routes.login :as login]
            [movies-app.util.extract :as util]
            [noir.session :as session]
            [noir.response :refer [redirect]]
            [noir.util.route :refer [restricted]]
            [hiccup.form :refer :all]))


(defn list-of-movies [movies]
  (for [movie movies]
    (let [ ratings (:ratings movie)
          posters (:posters movie)
          src "../img/details.png"
          ]
      [:div.movie
       (form-to [:get (str "/movie&" (:id movie)) ]                             
                [:img {:src (:thumbnail posters)}]                
                [:p.title (:title movie)]
                [:p "Audience rating: " (:audience_score ratings)]       
                [:p "Criticts rating: " (:critics_score ratings)]
                [:a {:href (str "/movie&" (:id movie)) :id "details" } [:img {:src "../img/details.png"}] ])       
       ]))
  )

(defn logout[]
  (layout/common
    [:div.logout
     [:p "Logged: "(session/get :username)
      [:a {:href "/logout"} "Logout"]
      [:a {:href "/edit-profile"} "Edit profile"]
      [:a {:href "/favorites"} "Favorite movies"]
      ]
     ]
    )  
  )

(defn home [movies search-term page]   
  (println page)
  (layout/common     
    (logout)
    [:h1 "Movies"]
    [:p "Welcome to powerful Tomatoer!"]
    [:br]    
    (form-to [:post "/search"] 
             (if-not (nil? search-term)
               (text-field {:value search-term} :search-term)
               (text-field {:placeholder "Enter search term"} :search-term))
             (submit-button {:id "search"} "Search"))
    (if-not (or (nil? movies) (empty? movies))
      (do
        (layout/common 
          (list-of-movies movies)          
          (if  (> (java.lang.Integer/parseInt page) 1)             
            (do      
              (layout/common              
                [:a {:href (str "/search&" (dec (java.lang.Integer/parseInt page)) "&" search-term)} [:img {:src "../img/prev.png"}]]
                (if (> (count movies) 9)      
                  [:a.next {:href (str "/search&" (inc (java.lang.Integer/parseInt page)) "&" search-term)} [:img {:src "../img/next.png"}]]                                                
                  )
                )
              ))
          
          (if (and (= (java.lang.Integer/parseInt page) 1) (> (count movies) 9))     
            (layout/common
              [:a {:href (str "/search&" 2 "&" search-term)} [:img {:src "../img/next.png"}]]            
              
              )         
            )
          )
        )
      (if (and (not-empty search-term) (or (nil? movies) (empty? movies)))
        (layout/common
          [:p "Sorry, there is no more movies :("]
          )
        )    
      )
    )
  )
  
  (defn search-for-movies [search-term page]
    (println "Searching for movies..." search-term)
    (let [links-url (util/get-links-url)
          template-url (util/get-template-url links-url)
          movie-url (util/get-movies-url template-url search-term page)
          movies-data (util/get-movies-data movie-url)]    
      (home movies-data search-term page)))
  
  
  (defroutes home-routes
    (GET "/home" [] (restricted (home nil nil 1)))  
    (GET "/search&:page&:search-term" [page search-term]  (restricted (search-for-movies search-term page)))
    (POST "/search" [search-term] (restricted (search-for-movies search-term "1")))
    (GET "/logout" [] (session/remove! :username) (redirect "/login"))  
    )
  
  
  
  
  
  
  