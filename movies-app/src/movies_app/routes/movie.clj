(ns movies-app.routes.movie
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.routes.login :as login]
            [movies-app.routes.home :as home]
            [movies-app.util.extract :as util]
            [noir.session :as session]
            [noir.response :refer [redirect]]
             [noir.util.route :refer [restricted]]
            [clojure.string :as cs]
            [movies-app.util.extract :as util]
            [hiccup.form :refer :all]
            [movies-app.routes.reviews :as review])
  )

(defn display-rating [movie]
  (let [critics_rating (:critics_rating (:ratings movie))
        audience_rating (:audience_rating (:ratings movie))
        ]
 
  (layout/common
    [:h2 "Ratings"]   
    [:p "Critics score: " (:critics_score (:ratings movie))]
    [:p "Critics rating: " [:img {:src (str "../img/" critics_rating ".png") :title critics_rating}]]
    [:p "Audience score: "(:audience_score (:ratings movie))]
    [:p "Audience rating: " [:img {:src (str "../img/" audience_rating ".png" ) :title audience_rating}]]
    )
  )
      )
  

(defn display-genres [movie]
  (layout/common
    [:h2 "Genres"]
    [:ul]
    (for [genre (:genres movie)]
      [:li (cs/replace genre "," "") ]
      )
    [:ul]
    )
  )

(defn display-char [cast]
  (for [char cast]
    (cs/join char)
    ))

(defn display-cast [movie]
  (layout/common
    [:h2 "Cast"]
    [:ul]
    (for [cast (:abridged_cast movie)]
      [:li (:name cast) " as "  (display-char (:characters cast))]
      )
    [:ul]
    ))

(defn display-similar [movie]
  (let [similar-movies (util/get-similar-movies movie)]
    
    (layout/common 
      [:h2 "You might also like..."]
      (if (= (count similar-movies) 0)
        [:p "No similar movies to display."]
        )
      (for [movie similar-movies]
        [:a.similar {:href (str "/movie&" (:id movie)) } [:img {:src (:thumbnail (:posters movie)) :title (:title movie)}]]
        
        )
      )
    ))

(defn show-details[movie-id]
  (let [movie (util/get-details-movie movie-id)]
    
    (layout/common
    (home/logout)
      [:div.movie-details
      [:h1 "Details about movie"]
      [:p (session/get! :movie-exists)]
      [:img {:src (cs/replace (:detailed (:posters movie)) "_tmb" "_det")}]
      [:p "Title: " (:title movie)]
      [:p "Duration: " (:runtime movie) " mins"]
      [:p "Published: " (:year movie)]
      [:p "Synopsis: " (:synopsis movie)]
      (display-rating movie)
      (display-genres movie)
      (display-cast movie)
      [:a {:href (str "/reviews&" movie-id)} "Click to see reviews..."]
      (display-similar movie-id)
      (form-to [:post "/add-to-favorites"]
               [:input {:type "hidden" :name "movie" :value movie-id}]
               [:input {:type "hidden" :name "username" :value (session/get :username)}]
               (submit-button {:id "favBtn"} "Add to favorites")
               )
      ])
    ))

(defroutes movie-routes 
  (GET "/movie&:id" [id] (restricted (show-details id)))
  
  )