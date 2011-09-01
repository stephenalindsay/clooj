(ns clooj.nrepl
  (:require [clojure.tools.nrepl :as nrepl])
  (:import [javax.swing JOptionPane]
           [java.net ConnectException UnknownHostException]))

(def default-nrepl-port 11011)
(def default-nrepl-host "localhost")

(defn- get-host
  [message]
  (JOptionPane/showInputDialog nil message default-nrepl-host))

(defn- get-port
  [message]
  (when-let [port (JOptionPane/showInputDialog nil message default-nrepl-port)]
    (try
      (Integer/parseInt port)
      (catch NumberFormatException nfe 
        (JOptionPane/showMessageDialog nil 
           "Invalid port specified, should be numeric")))))

(defn- conx
  [host port]
  (try
    (nrepl/connect host port)
    (catch UnknownHostException uhe
      (JOptionPane/showMessageDialog nil
        (format "Can't connect to %s, please check that it is a valid host." host)))
    (catch ConnectException ce
      (JOptionPane/showMessageDialog nil
         (str "Unable to connect to nREPL. \n\nPlease check that it is running "
              "and that you have entered the correct host and port. \n\n" 
              "Error: [" (.getMessage ce) "]")))))
                                     
(defn get-nrepl-conx
  [app]  
  (let [host (get-host "Connect to nREPL on which host?")
        port (when host (get-port "Connect to nREPL on which port?"))]
    (when (and host port)
      (println (format "Connecting with on host/port : %s/%s" host port))
      (conx host port))))
