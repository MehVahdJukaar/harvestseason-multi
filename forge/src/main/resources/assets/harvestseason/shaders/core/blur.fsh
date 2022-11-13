#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

uniform float Radius;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    float Pi = 6.28318530718; // Pi*2

    // GAUSSIAN BLUR SETTINGS {{{
    float Directions = 16.0; // BLUR DIRECTIONS (Default 16.0 - More is better but slower)
    float Quality = 50.0; // BLUR QUALITY (Default 4.0 - More is better but slower)
    float Size = 50.0 / 512.0; // BLUR SIZE (Radius)
    // GAUSSIAN BLUR SETTINGS }}}

    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = texCoord0;
    // Pixel colour
    vec4 Color = texture(Sampler0, uv);

    // Blur calculations
    for (float d = 0.0; d < Pi; d += Pi / Directions)
    {
        float inc = 1.0 / Quality;
        for (float i = inc; i <= 1.0; i += inc)
        {
            vec4 s = texture(Sampler0, uv + vec2(cos(d), sin(d)) * Size * i);
            if (s.x < 0.5) {
                s = vec4(0, 0, 0, 0);
            } else {
                s = vec4(0, 0, 0, 1);
            }
            Color += s;
        }
    }

    // Output to screen
    Color /= Quality * Directions - (Directions - 1);
    fragColor = Color;
}
