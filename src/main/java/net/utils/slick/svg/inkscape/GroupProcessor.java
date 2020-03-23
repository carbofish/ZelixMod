package net.utils.slick.svg.inkscape;

import net.utils.slick.geom.Transform;
import net.utils.slick.svg.Diagram;
import net.utils.slick.svg.Loader;
import net.utils.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * TODO: Document this class
 *
 * @author kevin
 */
public class GroupProcessor implements ElementProcessor {

	/**
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#handles(org.w3c.dom.Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("g")) {
			return true;
		}
		return false;
	}

	/**O
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#process(net.utils.slick.svg.Loader, org.w3c.dom.Element, net.utils.slick.svg.Diagram, net.utils.slick.geom.Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
		Transform transform = Util.getTransform(element);
		transform = new Transform(t, transform);
		
		loader.loadChildren(element, transform);
	}

}
