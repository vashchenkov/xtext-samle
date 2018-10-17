import com.google.inject.Injector;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.xbase.interpreter.IEvaluationResult;
import org.eclipse.xtext.xbase.interpreter.impl.DefaultEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.impl.XbaseInterpreter;
import ru.iqsocial.PublicDslStandaloneSetup;
import ru.iqsocial.publicDsl.Model;
import ru.iqsocial.publicDsl.NamedExpression;

import java.io.IOException;

public class ExampleTest {

	public static void main(String[] args) throws Throwable {

		try {
			final String userInput = "model x {\n" +
					"            a = 1\n" +
					"            b = 1 + 2\n" +
					"            c = \"Hello\"\n" +
					"        }";

			final PublicDslStandaloneSetup dslStandaloneSetup = new PublicDslStandaloneSetup();
			final Injector injector = dslStandaloneSetup.createInjectorAndDoEMFRegistration();
			final ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
			final Resource resource = resourceSet.createResource(URI.createURI("publicDsl"));
			resource.load(new StringInputStream(userInput), null);
			final Model model = (Model) resource.getContents();
			final XbaseInterpreter interpreter = injector.getInstance(XbaseInterpreter.class);

			for (NamedExpression expression : model.getExpressions()) {
				final DefaultEvaluationContext ctx = new DefaultEvaluationContext();
				final IEvaluationResult result = interpreter.evaluate(expression.getBody(), ctx, CancelIndicator.NullImpl);
				System.out.println("result.result = " + result.getResult());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
