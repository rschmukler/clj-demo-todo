(ns do.lib.todo
  (:require [mount.core :refer [defstate]]
            [malli.core :as m]))


(defstate db
  :start (atom {})
  :stop (reset! db {}))

(def create-todo-schema
  [:map
   {:closed true}
   [:todo/title string?]
   [:todo/done? boolean?]])

(defn create-todo!
  "Create a todo using the provided data"
  [data]
  (when-some [errors (m/explain create-todo-schema data)]
    (throw (ex-info "Invalid Todo" {:type :unprocessable-entity
                                    :errors errors})))
  (let [id         (random-uuid)
        final-data (assoc data :todo/id id)]
    (swap! db assoc id final-data)
    final-data))

(defn list-todos
  "Get all todos in the database"
  []
  (into [] (vals @db)))

(defn delete-todo!
  "Remove the todo with `id` from the database"
  [id]
  (swap! db dissoc id)
  true)

(defn complete-todo!
  "Set the provided `todo` as done in the database"
  [id]
  (swap! db update id assoc
         :todo/done? true
         :todo/completed-at (java.util.Date.)))
