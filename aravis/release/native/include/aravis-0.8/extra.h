#include <arv.h>
#include <glib-object.h>

typedef void (*NewBufferCallback)	 (ArvStream *stream, GObject *data);
typedef void (*ControlLostCallback)	 (ArvGvDevice *gv_device);

gulong arvx_signal_connect_new_buffer(gpointer		  instance,
					       NewBufferCallback	  callback,
					       gpointer		  data) {
    return g_signal_connect(instance, "new-buffer", G_CALLBACK(callback), data);
}

gulong arvx_signal_connect_control_lost(gpointer		  instance,
					       ControlLostCallback	  callback,
					       gpointer		  data) {
    return g_signal_connect(instance, "control-lost", G_CALLBACK(callback), data);
}

gulong g_signal_connect_x(gpointer		  instance,
					       const gchar	 *detailed_signal,
					       GCallback	  callback,
					       gpointer		  data) {
    return g_signal_connect(instance, detailed_signal, G_CALLBACK(callback), data);
}
