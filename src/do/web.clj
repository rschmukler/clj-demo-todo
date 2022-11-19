(ns do.web
  (:require [mount.core :refer [defstate]]
            [ring.adapter.undertow :as ut]
            [do.web.todo :as web.todo]
            [reitit.ring :as ring]
            [reitit.coercion.malli]
            [reitit.ring.coercion :as coercion]))


(def handler
  (ring/ring-handler
    (ring/router
      ["/api"
       ["/todos"
        ["/" {:get  {:handler web.todo/list-todos-handler}
              :post {:handler    web.todo/create-todo-handler
                     :parameters {:body [:map
                                         {:closed true}
                                         [:title string?]]}}}]
        ["/:id"
         ["" {:delete {:handler web.todo/delete-todo-handler}}]
         ["/complete" {:post {:handler web.todo/mark-todo-complete-handler}}]]]]
      {:data {:coercion   reitit.coercion.malli/coercion
              :middleware [coercion/coerce-response-middleware
                           coercion/coerce-request-middleware]}})))

(defstate server
  :start
  (do @lib.todo/db
      (ut/run-undertow handler {:port 3000}))
  :stop
  (.stop server))

(comment
  (mount.core/start server)
  (mount.core/stop server))
