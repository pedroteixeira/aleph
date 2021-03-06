;;   Copyright (c) Zachary Tellman. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file epl-v10.html at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.

(ns aleph.http.utils)

(defn to-str [x]
  (if (keyword? x)
    (name x)
    (str x)))

(defn cookie->hash [cookie]
  (when cookie
    (apply hash-map
      (apply concat
	(map
	  #(let [pair (.split % "[=]")]
	     (list (first pair) (or (second pair) "")))
	  (.split cookie "[;]"))))))

(defn hash->cookie [cookie]
  (when cookie
    (if (map? cookie)
      (->> cookie
	(map #(str (to-str (first %)) "=" (second %)))
	(interpose ";")
	(apply str))
      cookie)))

(defn cookie [request]
  (-> request :headers (get "cookie") cookie->hash))

(defn query [request]
  (apply hash-map (-> request :query-string (.split "[;&=]"))))
