// Generated code from Butter Knife. Do not modify!
package pt.andrew.blisschallenge.screens;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import pt.andrew.blisschallenge.R;

public class ServiceStatusActivity_ViewBinding implements Unbinder {
  private ServiceStatusActivity target;

  @UiThread
  public ServiceStatusActivity_ViewBinding(ServiceStatusActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ServiceStatusActivity_ViewBinding(ServiceStatusActivity target, View source) {
    this.target = target;

    target._mainContainer = Utils.findRequiredView(source, R.id.serviceHealthMainContainer, "field '_mainContainer'");
    target._loader = Utils.findRequiredView(source, R.id.serviceHealthLoaderContainer, "field '_loader'");
    target._description = Utils.findRequiredViewAsType(source, R.id.serviceHealthDescription, "field '_description'", TextView.class);
    target._statusIcon = Utils.findRequiredViewAsType(source, R.id.serviceHealthStatusIcon, "field '_statusIcon'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServiceStatusActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._mainContainer = null;
    target._loader = null;
    target._description = null;
    target._statusIcon = null;
  }
}
