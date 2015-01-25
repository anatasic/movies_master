(ns movies-app.routes.movie
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.util.extract :as util]
            [hiccup.element :refer :all]
            [hiccup.core :refer :all]
            [clojure.string :as cs]
            [hiccup.form :refer :all]))

(defn get-details-about-movie [id]
  (util/get-details-movie id)
  )

(defn read-genres [data]  
  (for [item (:genres data)] 
    (layout/common
      [:ul]
      [:li item]
      )
    )
  )

(defn read-director [data]
  (for [item (:abridged_directors data)]
    (layout/common
      [:ul]
      [:li (:name item)]
      
      )))

(defn read-director-last [data]
  (last (for [item  (:abridged_directors data)] (str (:name item))))
  )


(defn read-cast [data] 
  (for [item (:abridged_cast data)]
    (layout/common
      [:ul
       [:li
        (:name item)  " as " 
        (butlast (for [character (:characters item)] (str character ", ") ))
        (last (for [character (:characters item)] (str character) ))
        ]]
      )))

(defn read-cast-last [data]
  (last 
    (for [item (:abridged_cast data)] (str (:name item)  " as " (butlast 
                                                                  (for [character (:characters item)] (str character ", ") )) 
                                           (last (for [character (:characters item)] (str character))))))
  )

(defn show-similar-movies [movie-id]
  (let [similar-movies (util/get-similar-movies movie-id)]
    (layout/common
      [:p "You might also like"]
      (for [similar similar-movies]
      [:a {:href (str "/movie&" (:id similar))} [:img {:src (:thumbnail (:posters similar))}]]
      ))
    
    )
  )

(defn show-reviews [movie] 
  (println (str "Next" (:next(:links(util/get-reviews-for-movie movie))))
  (println (str "Movie" movie)) 
  (let [reviews (util/get-reviews-for-movie movie)]    
    (layout/common
      [:p "Total number of reviews: " (:total reviews)]
      (form-to [:POST "/next-review"]
               (for [review (:reviews reviews)]
                 [:div.reviews
                  [:p "Critics: " (:critic review)]
                  [:p "Date: " (:date review)]
                  [:p "Freshness: " (:freshness review)]
                  [:p "Publication: " (:publication review)]
                  [:p "Quote: " (:quote review)]
                  [:p "Full review: " [:a {:href (:review (:links review)) } (str (:review (:links review)))]]                                    
                  ]               
                 )
              
               (if-not (nil? (:next(:links reviews)))
                 (do
                   [:input {:type "hidden" :name "next" :value (str(:next(:links reviews))) }]
                   [:input {:type "hidden" :name "movie" :value movie }]  
                   (submit-button "Next")
                   ))
               
               )
      )
    )))



(defn show-reviews-pagination [page movie]  

  (let [reviews (util/get-reviews-for-movie-pagination page)]   
    (layout/common
      [:p "Total number of reviews: " (:total reviews)]        
      (for [review (:reviews reviews)]        
        [:div.reviews
         [:p "Critics: " (:critic review)]
         [:p "Date: " (:date review)]
         [:p "Freshness: " (:freshness review)]
         [:p "Publication: " (:publication review)]
         [:p "Quote: " (:quote review)]
         [:p "Full review: " [:a {:href (:review (:links review)) } (str (:review (:links review)))]]
         
         ]
        )
      
      ;                 (println (str "Previous "(:prev(:links reviews))))
      (if-not (nil? (:prev(:links reviews)))
        (do
          (form-to [:POST "/prev-review"]
                   [:input {:type "hidden" :name "prev" :value (str(:prev(:links reviews))) }]
                   [:input {:type "hidden" :name "movie-id" :value movie }]  
                   (submit-button "Previous")
                   )))
      (if-not (nil? (:next(:links reviews)))
        (do
          (form-to [:POST "/next-review"]
                   [:input {:type "hidden" :name "next" :value (str(:next(:links reviews))) }]
                   [:input {:type "hidden" :name "movie" :value movie }]  
                   (submit-button "Next")
                   )
          )))
    
    ))

(defn details [movie]
 
  (form-to [:post "/favorite"]
           [:h1 "Detailed info about movie"]
           [:div.details
           [:img {:src (cs/replace (:detailed (:posters movie)) "_tmb" "_det")}]
            ]
           [:p "Title: "(:title movie)]
           [:p "Year: "(:year movie)]
           [:p "Genres: "  (read-genres movie)  ]
           [:p "Duration: " (:runtime movie) " mins"]
           [:p "Directed by: "  (read-director movie)  ]
           [:p "Ratings"]
           [:p "Audience score: " (:audience_score(:ratings movie))]
           [:p "Audience rating: " (:audience_rating(:ratings movie))]
           [:p "Critics score: " (:critics_score(:ratings movie))]
           [:p "Critics rating: " (:critics_rating(:ratings movie))]
           [:p "Synopsis: " (:synopsis movie)]
           [:hr]
           [:h2 "Cast"]
           (read-cast movie)
           [:hr]
           ))



(defn show-details [movie-id]
  (let [movie (get-details-about-movie movie-id)]
    (layout/common
      (details movie)
      [:h2 "Top reviews"]   
      (show-reviews movie-id)      
      [:hr]
      [:h2 "Similar movies"]
;      (show-similar-movies movie-id)    
      (submit-button "Add to favorites")
      )))

(defn show-details-next [movie-id next]
  (let [movie (get-details-about-movie movie-id)]
    (layout/common
      (details movie)
      [:h2 "Top reviews"]   
      (show-reviews-pagination next movie-id)      
      [:hr]
      [:h2 "Similar movies"]
;      (show-similar-movies movie-id)    
      (submit-button "Add to favorites")
      )))

(defn show-details-prev [movie-id prev]  
  (let [movie (get-details-about-movie movie-id)]
    (layout/common
      (details movie)
      [:h2 "Top reviews"]   
      (show-reviews-pagination prev movie-id)      
      [:hr]
      [:h2 "Similar movies"]
      [:p "Critics score: " ]      
      (submit-button "Add to favorites")
      )))

(defn show-favorite []
 
  )

(defn next-review [next movie]
  (println (str "Next " next "Movie" movie))
  (show-details-next movie next)
  )

(defn prev-review [prev movie]
  (show-details-prev movie prev)
  )

(defroutes movie-routes
  (GET "/movie&:id" [id] (show-details id))
  (POST "/prev-review" [prev movie-id] (prev-review prev movie-id))
  (POST "/next-review" [next movie] (next-review next movie))
  )
