import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.isolated
import org.openrndr.extensions.Screenshots


fun main() {

    application {

        configure {
            width = 800
            height = 800
            windowResizable = true
        }

        program {
            class A : Animatable() {
                var x: Double = 0.0
                var y: Double = 0.0
                var radius: Double = 0.0
                var rotation: Double = 0.0
                var shade = 0.0
                fun next() {
                    updateAnimation()
                    if (!hasAnimations()) {

                        val speed  = (Math.random()*1000.0+1000.0).toLong()

                        var tx = Math.random() * width
                        var ty = Math.random() * height
                        val tr = Math.random() * 100.0


                        animate("x", tx, speed, Easing.CubicIn)
                        animate("y", ty, speed, Easing.CubicIn)
                        animate("radius", tr, speed, Easing.CubicIn)
                        animate("shade", Math.random() ,speed, Easing.CubicIn)
                        animate("rotation", Math.random()* 360.0, speed/2,Easing.CubicIn )
                        complete()
                        animate("rotation", Math.random()* 360.0, speed/2, Easing.CubicIn)
                    }
                }
            }

            val balls = mutableListOf<A>()

            for (i in 0 until 100) {
                balls.add(A())
            }

            val font = FontImageMap.fromUrl("file:data/fonts/SUBWT___.ttf", 32.0)

            extend(Screenshots())
            extend {
                for (a in balls) {

                    a.next()
                    //drawer.circle(a.x, a.y, a.radius)

                    //drawer.rectangle(a.x, a.y, a.radius, a.radius)
                    drawer.fontMap = font


                    drawer.isolated {
                        drawer.translate(a.x, a.y)
                        drawer.rotate(a.rotation)
                        drawer.translate(-a.x, -a.y)
                        drawer.fill = ColorRGBa.YELLOW.shade(a.shade)
                        drawer.text("HELLO", a.x, a.y)
                        drawer.fill = ColorRGBa.PINK
                        drawer.circle(a.x, a.y, a.radius)
                    }
                }
            }
        }

    }
}