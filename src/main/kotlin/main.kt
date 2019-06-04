


import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.isolated
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.compositor.Layer
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer
import java.io.File
import java.time.LocalDateTime

fun main() = application {
    configure {
        width = 2560
        height = 800
        windowResizable = true
    }

    program {

        val font = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Regular.ttf", 64.0)

        val posterWidth = 500.0
        val posterHeight = 700.0

        
        class Poster(val layer:Layer, val x:Double, val y:Double)



        fun makePoster(): Poster {
            val poster = compose {
                val a = Math.floor(Math.random()*10)
                val image = loadImage("file:data/archive/$a/images.jpg")
                val x1 = Math.random()*500
                val y1 = Math.random()*700
                val text1 = File("data/archive/1/text.txt").readLines()

                layer {
                    draw {
                        drawer.fill = ColorRGBa.BLUE
                        drawer.stroke = ColorRGBa.BLACK
                        drawer.rectangle(0.0, 0.0, posterWidth, posterHeight)
                    }


                }

                layer {

                    draw {
                        //drawer.image(image1,x1,y1-300,400.0,300.0)
                        drawer.image(image, 0.0, 0.0, image.width*0.5, image.height*0.5)
                    }
                }

                layer {

                    draw {
                        val font = FontImageMap.fromUrl("file:data/fonts/Rumeur/rumeur.otf", 30.0)
                        drawer.fontMap = font
                        drawer.fill = ColorRGBa.WHITE
                        //drawer.translate(x1, y1 - 260)



                        val w = Writer(drawer)

                        w.box = Rectangle(0.0, 400.0, 300.0, posterHeight-400.0)

                        for (line in text1) {
                            w.newLine()
                            w.text(line)
                            //drawer.text(line, 0.0, 100.0)
                        }
                    }
                }

            }
            return Poster(poster, Math.random()*width, Math.random()*100.0)
        }


        val posters = mutableListOf<Poster>()

        for (i in 0 until 5) {
            posters.add(makePoster())
        }

        extend {

            for (poster in posters) {
                drawer.isolated {
                    drawer.translate(poster.x, poster.y)
                    poster.layer.draw(drawer)
                }

            }


        }
    }
}