(ns api-testing.core
  (:import (java.util.function ToDoubleBiFunction)))

  (require '[ring.adapter.jetty :refer [run-jetty]])
  (require '[compojure.core :refer [defroutes GET DELETE POST PUT PATCH]])
  (require '[compojure.route :as route])
  (require '[muuntaja.middleware :refer [wrap-format]])
  (require '[ring.middleware.params :refer [wrap-params]])
  (require '[ring.middleware.json :refer [wrap-json-body]])


(defn success-response [body]
  {:body body :status 200})

(defn created-response [body]
  {:body body :status 201})

(defn error-response [body]
  {:body body :status 400})

(defn not-found-response [body]
  {:body body :status 404})


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
(defn helper-update-user [id new-user]
  (let [user (get-user id)
        updated-user (merge user new-user)]
    (swap! users assoc id updated-user)
    updated-user))

(defn helper-email-exists? [email]
  (let [list-users (vals @users) ;vals only returns only the values from tha map https://clojuredocs.org/clojure.core/vals
        emails (map (fn [user] (:email user)) list-users) ;this map extracts email key from every element from the list
        result (some (fn [candidate-email] ;some finds if exist in the list any email that is same than argument
                       (= email candidate-email)
                       )emails)]
    (true? result)))  ;it only returns true if the result argument = true


(defn get-users
[request]
(success-response @users))

(defn get-user-by-id                                        ;TODO GET-USER-BY-ID
  [request]
  (let [id (:id (:params request))]
        (if (nil? id)
          (error-response "error missing id")
          (success-response (get-user id))
          )))

(defn delete-user
[request]
  (let [params (:query-params request)
        id (get params "id")]
    (if (nil? id)
      (error-response "error missing id")
      (let [user (get-user id)]
        (if (nil? user)
          (not-found-response "User not found")
          ;{:body "User not found" :status 404}
          (success-response (del-user id))
         )))))

(defn create-user
  [request]
    (let [body (:body-params request)
          email (body :email)]
    (println email)
    (println body)
    (if (helper-email-exists? email)
      {:body "Email is already taken" :status 409}
      (if (nil? body)
        (error-response "error missing body")
        (created-response (add-user body))
        ))
    ))

(defn update-user
  [request]
  (let [body (:body-params request)
        email (body :email)
        id (:id (:params request))]
    (println body id)
    (if (helper-email-exists? email)
      {:body "Email is already taken" :status 409}
      (if (nil? id)
        (error-response "error missing id")
        (let [user (get-user id)]
          (if (nil? user)
            (not-found-response "User bit found")
            (success-response (helper-update-user id body))
            ))
        ))
    ))

(defn partial-update-user [request]
  (let [body (:body-params request)
        id (:id (:params request))]
    (println body id)
    (if (nil? id)
      (error-response "error missing id")
      (let [user (get-user id)]
        (if (nil? user)
          (not-found-response "User not found")
          (success-response (helper-update-user id body))
          )))))

(defroutes routes
           (GET "/users" request get-users)
           (GET "/users/:id" request get-user-by-id)
           (DELETE "/users" request delete-user)
           (POST "/users" request create-user)
           (PUT "/users/:id" request update-user)
           (PATCH "/users/:id" request partial-update-user)
           (route/not-found "Not the route you are looking for"))


(def app (run-jetty (wrap-format (wrap-json-body (wrap-params routes))) {:port 8080
                                                                         :join? false}))
