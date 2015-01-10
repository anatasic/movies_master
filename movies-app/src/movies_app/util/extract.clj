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
  (let [r (client/get url)
        code (:status r)
        body (:body r)]
    [code body]))

(defn get-links-url []
  (let [[code body] (scoop-url (str "http://api.rottentomatoes.com/api/public/v1.0.json?apikey=" (get-api-key)))]
    (when (= code 200) 
      (:movies (:links (cheshire/parse-string body true))))))

(defn get-template-url [links-url]
  (println (str links-url "?apikey=" (get-api-key)))
  (let [[code body] (scoop-url (str links-url "?apikey=" (get-api-key)))]
    (when (= code 200) 
      (:link_template (cheshire/parse-string body true)))))

(defn get-movies-url [template search-term]
  (println template)
  (let [search (cs/replace template "{search-term}" (URLEncoder/encode search-term))
        results (cs/replace search "{results-per-page}" "50")
        page (cs/replace results "{page-number}" "1")
        fin (str page "&apikey=" (get-api-key))]
    fin) 
  )

(defn get-movies-data [movie-url]
  (let [[code body] (scoop-url movie-url)]
    (if (= code 200)
      (:movies (cheshire/parse-string body true))     
      nil)))