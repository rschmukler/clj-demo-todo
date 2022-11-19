(ns do.web.todo)

(defn valid-todo-middleware
  [req]
  (println "I was called")
  req)

(defn create-todo-handler
  [req]
  {:status 200
   :body   "Create"})

(defn mark-todo-complete-handler
  [req]
  {:status 200
   :body "Complete"})

(defn list-todos-handler
  [req]
  {:status 200
   :body   "List"}
  )

(defn delete-todo-handler
  [req]
  {:status 200
   :body   "Delete"})
