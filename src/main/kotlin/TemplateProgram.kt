import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.multiply
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer
import org.openrndr.workshop.toolkit.filters.VerticalStepWaves
import java.io.File
import java.time.LocalDateTime

fun main() = application {
    configure {
        width = 2560
        height = 1500
        windowResizable = true

    }

    program {

        val font = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Regular.ttf", 64.0)

        val posterWidth = 400.0
        val posterHeight = 650.0

        class Brush: Animatable(){
            var scale = 1.0
            var rotation  = 0.0
            var x: Double = 0.0
            var y: Double = 0.0
        }

        class Poster(val layer:Layer, val x:Double, val y:Double, val brush:Brush)

        val numbers = (1 .. 10).toList().shuffled()
        val fontNumbers = (1 .. 15).toList().shuffled()




        fun makePoster(index:Int): Poster {
            var brush = Brush()
            val poster = compose {
                val a = numbers[index]//(Math.random()*10+1).toInt()\
                val f = fontNumbers[index]
                val r = Math.random()
                val g = Math.random()
                val b = Math.random()
                var tx = Math.random() * width
                var ty = Math.random() * height
                val image1 = loadImage("file:data/archive/$a/images.jpg")
                keyboard.character.listen{
                    if(it.character == 'r'){
                        brush.animate("rotation", Math.random() * 360.0,1000, Easing.QuadInOut)
                    }
                    if(it.character == 'd'){
                        brush.cancel()
                        brush.animate("scale", Math.random(), 1000, Easing.CubicInOut)
                    }
                }


                val text1 = File("data/archive/$a/text.txt").readLines()
                layer {

                    draw {
                        drawer.fill = ColorRGBa(r,g,b)
                        drawer.stroke = ColorRGBa.BLACK
                        drawer.rectangle(0.0, 0.0, posterWidth, posterHeight)
                    }


                }

                layer {
                    post(VerticalStepWaves()){
                        this.amplitude = 0.01
                        this.period = 8.0
                        this.phase = seconds * 5.5
                    }
                    draw {
                        val font = FontImageMap.fromUrl("file:data/fonts/$f/font.ttf", 35.0)
                        drawer.fontMap = font
                        drawer.fill = ColorRGBa.WHITE
                        //drawer.translate(x1, y1 - 260)



                        val w = Writer(drawer)

                        w.box = Rectangle(5.0, 400.0, 300.0, posterHeight-500.0)

                        for (line in text1) {
                            w.newLine()
                            w.text(line)
                            //drawer.text(line, 0.0, 100.0)
                        }
                    }
                }

                layer {

                    draw {
                        //drawer.image(image1,x1,y1-300,400.0,300.0)
                        drawer.image(image1, 0.0, 0.0, 398.0, 350.0)
                    }
                }

               




            }
            return Poster(poster, Math.random()*(width-posterWidth), Math.random()*(height - posterHeight), brush)

        }


        val posters = mutableListOf<Poster>()


        for (i in 0 until 5) {
            posters.add(makePoster(i))
        }

        mouse.clicked.listen {
            posters.clear()

            for (i in 0 until 5) {

                posters.add(makePoster(i))
            }

        }

        extend {

            for (poster in posters) {
                poster.brush.updateAnimation()
                if (!poster.brush.hasAnimations()) {
                    poster.brush.animate("x", Math.random() * width, 3000, Easing.QuadInOut)
                    poster.brush.animate("y", Math.random() * height, 3000, Easing.QuadInOut)
                }
                drawer.isolated {
                    drawer.translate(poster.brush.x, poster.brush.y)
                    drawer.rotate(poster.brush.rotation)
                    drawer.scale(poster.brush.scale)
                    poster.layer.draw(drawer)

                }

            }


        }
    }
}