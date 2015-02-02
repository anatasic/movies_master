(ns movies-app.routes.reviews
  (:require [compojure.core :refer :all]
            [movies-app.views.layout :as layout]
            [movies-app.routes.home :as home]
            [movies-app.util.extract :as util]
            [noir.session :as session]
            [noir.response :refer [redirect]]
            [clojure.string :as cs]
            [movies-app.util.extract :as util]
            [noir.util.route :refer [restricted]]
            [hiccup.form :refer :all])
  )

(defn get-full-review [review]

     (:review (:links review)))
    
;(defn review [reviews]
;
;  (for [review reviews]
;    (layout/common
;      [:p "Critic: "(:critic review)]
;      [:p "Date: "(:date review)]
;      [:p "Freshness: " (:freshness review)]
;      [:p "Published by: " (:publication review)]
;      [:p "Quote: " (:quote review)]
;      [:p "Full review: " [:a {:href (get-full-review review)} (get-full-review review)]]
;      [:hr]
;      )      
;    )
;  (if-not (nil? (:next(:links reviews)))
;    (form-to [:POST "/next-page-review"]
;             [:input {:type "hidden" :name "next" :value ((:next(:links reviews)))}]
;             ;      [:input {:type "hidden" :name "movie" :value id}]
;             (submit-button "Next")
;             )
;    )
;  (if-not (nil? (:prev(:links reviews)))
;    (form-to [:POST "prev-page-review"]
;             [:input {:type "hidden" :name "prev"  :value ((:prev(:links reviews)))}]
;             ;                 [:input {:type "hidden" :name "movie" :value id}]
;             (submit-button "Previous")
;             )
;    )
;  )

(defn display-reviews [id]
  (let [reviews (util/get-reviews-for-movie id)]
 
    (layout/common
      (home/logout)
      [:h1 "Top reviews"]
      [:p "Total number of reviews: " (:total reviews)]
      (for [review (:reviews reviews)]
        (do
         (let [full-review (get-full-review review)]
        (layout/common
          [:p "Critic: "(:critic review)]
          [:p "Date: "(:date review)]
          [:p "Freshness: " (:freshness review)]
          [:p "Published by: " (:publication review)]
          [:p "Quote: " (:quote review)]
          [:p "Full review: " [:a {:href (get-full-review review) :rel "canonical"} (get-full-review review)]]
          [:hr]
          )      
        )))
      (if-not (nil? (:next(:links reviews)))
        ;      (println (:next(:links reviews)))
        (form-to [:post "/next-page-review"]
                 [:input {:type "hidden" :name "next" :value (:next(:links reviews))}]
                 [:input {:type "hidden" :name "movie" :value id}]
                 (submit-button {:id "pagination"}"Next")
                 )
        )
      (if-not (nil? (:prev(:links reviews)))
        (form-to [:post "/prev-page-review"]
                 [:input {:type "hidden" :name "prev"  :value (:prev(:links reviews))}]
                 [:input {:type "hidden" :name "movie" :value id}]
                 (submit-button {:id "pagination"}"Previous")
                 )
        )    
      )
    
    )
  )

(defn display-reviews-pagination [id reviews]
    (layout/common
      [:h1 "Top reviews"]
      [:p "Total number of reviews: " (:total reviews)]
      (for [review (:reviews reviews)]
        (layout/common
          [:p "Critic: "(:critic review)]
          [:p "Date: "(:date review)]
          [:p "Freshness: " (:freshness review)]
          [:p "Published by: " (:publication review)]
          [:p "Quote: " (:quote review)]
          [:p "Full review: " [:a {:href (str "/full-review&"(get-full-review review)) :rel "canonical"} (get-full-review review)]]
          [:hr]
          )      
        )
       (if-not (nil? (:prev(:links reviews)))
        (form-to {:id "next"} [:post "/prev-page-review"]
                 [:input {:type "hidden" :name "prev"  :value (:prev(:links reviews))}]
                 [:input {:type "hidden" :name "movie" :value id}]
                 (submit-button {:id "pagination"} "Previous")
                 )
        )
      (if-not (nil? (:next(:links reviews)))
        ;      (println (:next(:links reviews)))
      (form-to {:id "next"} [:post "/next-page-review"]
                 [:input {:type "hidden" :name "next" :value (:next(:links reviews))}]
                 [:input {:type "hidden" :name "movie" :value id}]
                 (submit-button {:id "pagination"}"Next")
                 )
        )
         
      
    
    )
  
  )

(defn display-next-page [movie next]
  (let [reviews (util/get-reviews-for-movie-pagination next)]
  (display-reviews-pagination movie reviews)
  ))


(defn display-previous-page [movie prev]
  (let [reviews (util/get-reviews-for-movie-pagination prev)]
  (display-reviews-pagination movie reviews)
  ))

(defroutes reviews-routes
  (GET "/reviews&:id" [id] (restricted (display-reviews id)))
  (POST "/next-page-review" [next movie] (restricted (display-next-page movie next)))
  (POST "/prev-page-review" [movie prev] (restricted (display-previous-page movie prev)))
  (GET "/full-review&:link" [review] (redirect (get-full-review review)))
  )