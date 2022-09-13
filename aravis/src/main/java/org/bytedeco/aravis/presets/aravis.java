package org.bytedeco.aravis.presets;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.tools.*;

@Properties(
    value = {
        @Platform(
            includepath = {"/usr/include/glib-2.0/", "/usr/lib64/glib-2.0/include/"},
            include = {
                "<patch.h>",
                "<glib/gerror.h>",
                "<glib/gtypes.h>",
                "<glib/gdataset.h>",
                "<glib/gnode.h>",
                "<glib/glist.h>",
                "<glib/gslist.h>",
                "<gobject/gtype.h>",
                "<gio/giotypes.h>",
                "<gobject/gobject.h>",
                "<gobject/gsignal.h>",
                "<gobject/gclosure.h>",
                "<gobject/gparam.h>",
                "<arv.h>",
                "<arvenums.h>",
                "<arvfeatures.h>",
                "<arvtypes.h>",
                "<arvbuffer.h>",
                "<arvcamera.h>",
                "<arvchunkparser.h>",
                "<arvdebug.h>",
                "<arvstream.h>",
                "<arvdevice.h>",
                "<extra.h>",
            },
            link = {
                "aravis-0.8",
            }),
    },
    target = "org.bytedeco.aravis",
    global = "org.bytedeco.aravis.global.aravis"
)
public class aravis implements InfoMapper {
    static { Loader.checkVersion("org.bytedeco", "aravis"); }

    public void map(InfoMap infoMap) {
        infoMap.put(new Info("ARV_API", "G_BEGIN_DECLS", "G_END_DECLS",
                    "GLIB_DEPRECATED_IN_2_36", "GLIB_DEPRECATED_IN_2_38",
                    "GLIB_AVAILABLE_IN_2_32", "GLIB_AVAILABLE_IN_2_34", "GLIB_AVAILABLE_IN_2_36", "GLIB_AVAILABLE_IN_2_38", "GLIB_AVAILABLE_IN_2_42", "GLIB_AVAILABLE_IN_2_44", "GLIB_AVAILABLE_IN_2_46", "GLIB_AVAILABLE_IN_2_54",
                    "GLIB_AVAILABLE_IN_ALL", "GLIB_DEPRECATED",
                    "ARV_TYPE_PIXEL_FORMAT", "G_TYPE_UINT32", "G_TYPE_FLAG_RESERVED_ID_BIT", "G_TYPE_FUNDAMENTAL_MAX",
                    "_GLIB_EXTERN", "GLIB_VAR",
                    "G_GNUC_END_IGNORE_DEPRECATIONS", "extern", "g_slist_free1", "g_list_free1", "G_PARAM_DEPRECATED", "G_PARAM_STATIC_STRINGS"
                    ).cppTypes().annotations())

                // Need explicit casts
                .put(new Info("GCallback").pointerTypes("@Cast(\"GCallback*\") GCallbackPointer"))
                .put(new Info("ArvStreamCallback").pointerTypes("@Cast(\"ArvStreamCallback*\") ArvStreamCallbackPointer"))
                .put(new Info("NewBufferCallback").pointerTypes("@Cast(\"NewBufferCallback*\") NewBufferCallbackPointer"))
                .put(new Info("ControlLostCallback").pointerTypes("@Cast(\"ControlLostCallback*\") ControlLostCallbackPointer"))

                // Remove this method; can't figure out the data closure
                .put(new Info("g_signal_connect_data").skip())

                // Comments cause unicode error, don't know how to fix
                .put(new Info("_GTypeValueTable").skip())
                .put(new Info("GSocketSourceFunc").skip())
                .put(new Info("GDBusProxyTypeFunc").skip())
                .put(new Info("GCancellableSourceFunc").skip())
                .put(new Info("GDatagramBasedSourceFunc").skip())
                .put(new Info("GAsyncReadyCallback").skip())
                .put(new Info("GFileProgressCallback").skip())
                .put(new Info("GIOSchedulerJobFunc").skip())
                .put(new Info("GFileProgressCallback").skip())
                .put(new Info("GFileMeasureProgressCallback").skip())
                .put(new Info("GPollableSourceFunc").skip())
                .put(new Info("GFileReadMoreCallback").skip())
                
                // We've disabled USB
                .put(new Info("ARAVIS_HAS_USB").define(false))

                //.put(new Info("ArvBuffer").base("Pointer"))

                // Name conflicts
                .put(new Info("_GParamSpecClass::finalize").javaNames("_finalize"))
                .put(new Info("_GParamSpecTypeClass::finalize").javaNames("_finalize"))
                .put(new Info("_GParamSpecTypeInfo::finalize").javaNames("_finalize"))
                .put(new Info("_GObjectClass::finalize").javaNames("_finalize"))

                // Not sure
                .put(new Info("_GObject::qdata").skip())

                // 
                .put(new Info("g_list_free1").skip())
                .put(new Info("g_slist_free1").skip())
                .put(new Info("g_list_free_1").skip())
                .put(new Info("g_slist_free_1").skip())

                // Enums to ints
                .put(new Info("GConnectFlags").cast().valueTypes("int"))
                .put(new Info("ArvPixelFormat").cast().valueTypes("int"))
                .put(new Info("ArvRangeCheckPolicy").cast().valueTypes("int"))
                .put(new Info("ArvAccessCheckPolicy").cast().valueTypes("int"))
                .put(new Info("ArvGvPacketSizeAdjustment").cast().valueTypes("int"))
                .put(new Info("ArvGvStreamOption").cast().valueTypes("int"))
                .put(new Info("ArvGvIpConfigurationMode").cast().valueTypes("int"))
                .put(new Info("ArvGcAccessMode").cast().valueTypes("int"))
                .put(new Info("ArvRegisterCachePolicy").cast().valueTypes("int"))

                // Datatype redefinitions
                .put(new Info("GQuark").cast().valueTypes("int").pointerTypes("IntPointer"))
                .put(new Info("gpointer", "gconstpointer").cast().valueTypes("Pointer"))
                .put(new Info("gsize").cast().valueTypes("long").pointerTypes("LongPointer"))
                .put(new Info("gchar", "guint8", "gint8").cast().valueTypes("byte").pointerTypes("CharPointer"))
                .put(new Info("gint16", "guint16").cast().valueTypes("short").pointerTypes("ShortPointer"))
                .put(new Info("gint32", "guint32", "gint", "guint").cast().valueTypes("int").pointerTypes("IntPointer"))
                .put(new Info("gint64", "guint64", "glong", "gulong", "goffset").cast().valueTypes("long").pointerTypes("LongPointer"))
                .put(new Info("gdouble").cast().valueTypes("double").pointerTypes("DoublePointer"))
                .put(new Info("gfloat").cast().valueTypes("float").pointerTypes("FloatPointer"))
                .put(new Info("gboolean").cast().valueTypes("boolean").pointerTypes("BooleanPointer"))
                ;
    }
}
