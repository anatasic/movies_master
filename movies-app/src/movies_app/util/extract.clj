(ns movies-app.util.extract
  (:require
    [clojure.string :as cs]
    [clj-http.client :as client]
    [cheshire.core :as cheshire]))

(import [java.net URLEncoder])

(defn get-api-key []
  (str "cfu7ks8sbc9yur4sts9zayyx")
  )

(defn scoop-url [url]
  ("This function is used to get response code and body")
  (let [r (client/get url)
        code (:status r)
        body (:body r)]
    [code body]))

(defn get-links-url []
  ("This function is used to get the link for movies")
  (let [[code body] (scoop-url (str "http://api.rottentomatoes.com/api/public/v1.0.json?apikey=" (get-api-key)))]
    (when (= code 200) 
      (:movies (:links (cheshire/parse-string body true))))))

(defn get-template-url [links-url]  
  ("This function is used to get the link template")
  (let [[code body] (scoop-url (str links-url "?apikey=" (get-api-key)))]
    (when (= code 200) 
      (:link_template (cheshire/parse-string body true)))))

(defn get-movies-url [template search-term page-number]
  ("This function is used to form the url that will be used to get the data about movies based on the search term")
  (let [search (cs/replace template "{search-term}" (URLEncoder/encode search-term))
        results (cs/replace search "{results-per-page}" "10")
        page (cs/replace results "{page-number}" (str page-number))
        fin (str page "&apikey=" (get-api-key))]
    fin) 
  )

(defn get-movies-data [movie-url]
  ("This function is used to get data about the movies"
  (let [[code body] (scoop-url movie-url)]
    (if (= code 200)
      (:movies (cheshire/parse-string body true))     
      nil)))

(defn get-details-movie [movie-id]
  ("This function is used to get details about selected movie")
  (let [url (str "http://api.rottentomatoes.com/api/public/v1.0/movies/" movie-id ".json?apikey=" (get-api-key))
        [code body] (scoop-url url)]
    (if (= code 200)
      (cheshire/parse-string body true)     
      nil)
    ))

(defn get-reviews-for-movie[movie-id]
  ("This function is used to get movie reviews")
  (let [url (str "http://api.rottentomatoes.com/api/public/v1.0/movies/" movie-id ".json?apikey=" (get-api-key))
        [code body] (scoop-url url)]
    (if (= code 200)         
      (let [reviews (:reviews (:links (cheshire/parse-string body true)))
            [code body] (scoop-url (str reviews "?apikey=" (get-api-key) ))]     
        (if (= code 200)
          (cheshire/parse-string body true)
          )))))

(defn get-reviews-for-movie-pagination[movie]
  ("This function is used to get movie reviews per specific page")
  (let [url (str movie "&apikey=" (get-api-key))
        [code body] (scoop-url url)]
    (if (= code 200)         
      (cheshire/parse-string body true)
      )))

(defn get-similar-movies[movie-id] 
  ("This function is used to get similar movies for selected movie")
  (let [url (str "http://api.rottentomatoes.com/api/public/v1.0/movies/" movie-id ".json?apikey=" (get-api-key))
        [code body] (scoop-url url)]
    (if (= code 200)              
      (let [similar-movies (:similar (:links (cheshire/parse-string body true)))
            [code body] (scoop-url (str similar-movies "?apikey=" (get-api-key) ))]     
        (if (= code 200)
          (:movies (cheshire/parse-string  body true))
          )))))

(defn read-review-next [next]
  ("This function is used to get reviews per specific page")
  (let [url (str  next "&apikey=" (get-api-key))
        [code body] (scoop-url url)]
    (if (= code 200)
      (cheshire/parse-string body true)
      )))