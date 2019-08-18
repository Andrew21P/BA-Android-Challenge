// Generated code from Butter Knife. Do not modify!
package pt.andrew.blisschallenge.screens;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import pt.andrew.blisschallenge.R;

public class QuestionsScreenActivity_ViewBinding implements Unbinder {
  private QuestionsScreenActivity target;

  @UiThread
  public QuestionsScreenActivity_ViewBinding(QuestionsScreenActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public QuestionsScreenActivity_ViewBinding(QuestionsScreenActivity target, View source) {
    this.target = target;

    target._questionsRecyclerView = Utils.findRequiredViewAsType(source, R.id.questionsScreenRecyclerView, "field '_questionsRecyclerView'", RecyclerView.class);
    target._loader = Utils.findRequiredView(source, R.id.questionsScreenLoaderContainer, "field '_loader'");
    target._emptyState = Utils.findRequiredView(source, R.id.questionsScreenEmptyState, "field '_emptyState'");
    target._tryAgainButton = Utils.findRequiredView(source, R.id.questionsScreenTryAgainButton, "field '_tryAgainButton'");
  }

  @Override
  @CallSuper
  public void unbind() {
    QuestionsScreenActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._questionsRecyclerView = null;
    target._loader = null;
    target._emptyState = null;
    target._tryAgainButton = null;
  }
}
