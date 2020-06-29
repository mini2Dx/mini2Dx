#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_overlay;

void main()
{
	vec3 textureColor = texture2D(u_texture, v_texCoords).rgb;
	vec3 overlayColor = texture2D(u_overlay, v_texCoords).rgb;

    vec3 result = (overlayColor * 0.5 + textureColor * (1.0 - 0.5));

	gl_FragColor = vec4(result, 1.0);
}
