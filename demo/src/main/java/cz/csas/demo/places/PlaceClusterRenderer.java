package cz.csas.demo.places;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import cz.csas.demo.R;
import cz.csas.demo.components.CsasMarker;
import cz.csas.places.Type;

/**
 * The type Place cluster renderer.
 */
public class PlaceClusterRenderer extends DefaultClusterRenderer<PlaceClusterItem> {

    private IconGenerator iconGenerator;
    private Context context;
    private float maxZoom = 21;
    private float zoom = 1;

    /**
     * Instantiates a new Place cluster renderer.
     *
     * @param context        the context
     * @param map            the map
     * @param clusterManager the cluster manager
     */
    public PlaceClusterRenderer(Context context, GoogleMap map, ClusterManager<PlaceClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        iconGenerator = new IconGenerator(context);
    }

    @Override
    protected void onBeforeClusterItemRendered(PlaceClusterItem item, MarkerOptions markerOptions) {
        CsasMarker csasMarker = new CsasMarker(context);

        Type type = item.getPlace().getType();

        if (type.equals(Type.ATM)) {
            csasMarker.setBackground(ContextCompat.getDrawable(context, R.drawable.marker_atm));
        } else if (type.equals(Type.BRANCH)) {
            csasMarker.setBackground(ContextCompat.getDrawable(context, R.drawable.marker_branch));
        }

        iconGenerator.setContentView(csasMarker);
        iconGenerator.setBackground(null);
        Bitmap icon = iconGenerator.makeIcon();

        markerOptions.anchor(1, 1)
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .title(item.getPlace().getName()).visible(true);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<PlaceClusterItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return zoom < maxZoom - 1.5f && cluster.getSize() > 10;
    }

    /**
     * Sets zoom.
     *
     * @param zoom the zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    /**
     * Sets max zoom.
     *
     * @param maxZoom the max zoom
     */
    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }

}
