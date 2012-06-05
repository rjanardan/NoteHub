(ns NoteHub.views.common
  (:use
    [cssgen]
    [cssgen.types]
    [noir.core :only [defpartial]]
    [hiccup.page :only [include-js html5]]
    [hiccup.element :only [javascript-tag]]))

(defn gen-comma-list [& fonts] (apply str (interpose "," fonts)))
(def page-width
  (mixin
    :width :800px))
(def helvetica-neue
  (mixin
    :font-weight 300
    :font-family (gen-comma-list "'Helvetica Neue'"
                                 "Helvetica"
                                 "Arial"
                                 "'Lucida Grande'"
                                 "sans-serif")))
(def central-element
  (mixin
    page-width
    :margin-top :5em
    :margin-bottom :10em
    :margin-left "auto"
    :margin-right "auto"))

(defn color [theme tone]
  (get-in {:dark-theme {:background :#fff
                        :foreground :#333
                        :background-halftone :#efefef
                        :foreground-halftone :#888 }
           :bright-theme {:background :#fff
                          :foreground :#333
                          :background-halftone :#efefef
                          :foreground-halftone :#888 }} [theme tone]))

(defn global-css [arg]
  (let [theme (if-not arg :bright-theme)
        background (mixin (color theme :background))
        foreground (color theme :foreground)
        background-halftone (color theme :background-halftone)
        foreground-halftone (color theme :foreground-halftone)]
  (css 
    (rule ".landing-button"
          :box-shadow [0 :2px :5px :#aaa]
          :text-decoration :none
          :font-size :1.5em
          :background :#0a2
          :color :white
          :border :none
          :border-radius :10px
          :padding :10px
          helvetica-neue
          (rule "&:hover"
                :background :#0b2))
    (rule "html, body"
          :background background
          :color foreground
          :margin 0
          :padding 0)
    (rule "#hero"
          :padding-top :5em
          :padding-bottom :5em
          :text-align :center
          (rule "h2"
                helvetica-neue))
    (rule "article"
          central-element
          :font-family :Georgia
          :font-size :1.2em
          (rule "& > h1:first-child"
                :text-align :center
                :margin :2em))
    (rule "pre"
          :border-radius :3px
          :padding :1em
          :border [:1px :dotted foreground-halftone]
          :background background-halftone)
    (rule "*:focus"
          :outline [:0px :none :transparent])
    (rule "textarea"
          page-width
          :font-family :Courier
          :font-size :1.2em
          :border :none
          ; TODO: make this dynamic
          :height :500px
          :margin-bottom :2em)
    (rule ".hidden"
          :display :none)
    (rule ".button"
          :border-radius :3px
          helvetica-neue
          :cursor :pointer
          :border [:1px :solid foreground]
          :opacity 0.8
          :font-size :1em
          :background background)
    (rule ".central-element"
          central-element)
    (rule "h1"
          :font-size :2em)
    (rule "#preview-start-line"
          :border-bottom [:1px :dashed foreground-halftone]
          :margin-bottom :5em)
    (rule "h1, h2, h3, h4" 
          :font-family (gen-comma-list "'Noticia Text'" "'PT Serif'")))))

(defpartial generate-layout 
            [params title & content]
            (html5
              [:head
               [:title "NoteHub &mdash; " title]
               [:link {:href (str "http://fonts.googleapis.com/css?family="
                                  "PT+Serif:700|Noticia+Text:700"
                                  "&subset=latin,cyrillic" )
                       :rel "stylesheet"
                       :type "text/css"}]
               [:style {:type "text/css"} (global-css (params :theme))]]
              (if (params :js)
                [:body content
                 (javascript-tag "var CLOSURE_NO_DEPS = true;")
                 (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js")
                 (include-js "/cljs/main.js")]
                [:body content])))

(defn layout [& args]
  (if (map? (first args))
    (apply generate-layout args)
    (apply generate-layout {} args)))

