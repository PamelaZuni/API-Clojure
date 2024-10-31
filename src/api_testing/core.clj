(ns api-testing.core
  (:import (java.util.function ToDoubleBiFunction)))

  (require '[ring.adapter.jetty :refer [run-jetty]])
  (require '[compojure.core :refer [defroutes GET DELETE POST PUT]])
  (require '[compojure.route :as route])
  (require '[muuntaja.middleware :refer [wrap-format]])
  (require '[ring.middleware.params :refer [wrap-params]])
  (require '[ring.middleware.json :refer [wrap-json-body]])


(def users (atom {"id1" {
                              :email "test@gmail.com"
                              :name "test"
                              }
                       "id2" {
                              :email "test2@gmail.com"
                              :name "test2"
                              }}))
(defn add-user [{id :id :as user}]
  (swap! users #(assoc % id user))
  id)


(defn del-user [id]
  (swap! users #(dissoc % id)))

(defn get-user [id]
  (get @users id))

; (defn helper-get-users []
;   @users)
(defn helper-updated-user [id new-user]
  (let [user (get-user id)
        updated-user (merge user new-user)]
    (swap! users assoc id updated-user)
    updated-user))


(defn get-users
[request]
{ :status 200
  :body @users})

(defn get-user-by-id                                        ;TODO GET-USER-BY-ID
  [request]
  (let [id (:id (:params request))]
        (if (nil? id)
          {:body "" :status 400}
          {:body (get-user id)  :status 200})))

(defn delete-user
[request]
  (let [params (:query-params request)
        id (get params "id")]
    (if (nil? id)
      {:body "error missing id" :status 400}
      (let [user (get-user id)]
        (if (nil? user)
          {:body "User not found" :status 404}
          {:body (del-user id) :status 200})))))

(defn create-user
  [request]
  (let [body (:body-params request)]
    (println body )
    (if (nil? body)
      {:body "error missing body" :status 400}
      {:body (add-user body) :status 201})
    ))

(defn update-user
  [request]
  (let [body (:body-params request)
        id (:id (:params request))]
    (println body id)
    (if (nil? id)
      {:body "error missing id" :status 400}
      (let [user (get-user id)]
        (if (nil? user)
          {:body "User not found" :status 404}
          {:body (helper-updated-user id

                                      body) :status 200}))

      )))

(defroutes routes
           (GET "/users" request get-users)
           (GET "/users/:id" request get-user-by-id)
           (DELETE "/users" request delete-user)
           (POST "/users" request create-user)
           (PUT "/users/:id" request update-user)
           (route/not-found "Not the route you are looking for"))


(def app (run-jetty (wrap-format (wrap-json-body (wrap-params routes))) {:port 8080
                                                                         :join? false}))
