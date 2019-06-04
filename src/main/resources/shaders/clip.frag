#version 330

in vec2 v_texCoord0;
uniform sampler2D tex0;
uniform sampler2D tex1;

out vec4 o_color;
void main() {
    vec4 a = texture(tex0, v_texCoord0);
    vec4 b = texture(tex1, v_texCoord0);

    vec3 nb = b.a == 0.0 ? vec3(0.0) : b.rgb / b.a;

    o_color = vec4(a.rgb * nb, a.a * b.a);


}