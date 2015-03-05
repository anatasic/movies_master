(ns movies-app.repl
  (:use movies-app.handler
        ring.server.standalone
        [ring.middleware file-info file]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.params :only [wrap-params]]
        [movies-app.models.database :only [init-db]])
  (:require [noir.session :as session]))

(defonce server (atom nil))

(defn get-handler []
  (-> #'app
    (wrap-stacktrace)
    (wrap-file "resources")
    (wrap-reload)
    (wrap-params)
    (wrap-file-info)))

(defn start-server
  "Used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 9001)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :init init
                    :auto-reload? true
                    :destroy destroy
                    :join true}))
    (init-db)
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn -main [& args]
  (start-server))
