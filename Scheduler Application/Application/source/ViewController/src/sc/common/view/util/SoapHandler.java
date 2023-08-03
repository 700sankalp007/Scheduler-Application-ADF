package sc.common.view.util;

import java.io.StringWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import oracle.adf.share.logging.ADFLogger;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Element;

public class SoapHandler implements SOAPHandler<SOAPMessageContext> {
    private static final String KEY_REQUEST = SoapHandler.class.getName() + ".Request";
    private static final String KEY_START_TIME = SoapHandler.class.getName() + ".StartTime";
    private static final Level LOG_LEVEL;
    private static final ADFLogger internalLogger = ADFLogger.createADFLogger(SoapHandler.class);
    private static final long NANOSECS_IN_MILLISECS = 1000000;

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public void close(MessageContext arg0) {
        // TODO Auto-generated method stub
        arg0.remove(KEY_REQUEST);
        arg0.remove(KEY_START_TIME);

    }

    public ADFLogger getServiceLogger(final MessageContext context) {
        final String loggerName =
            SoapHandler.class.getName() + "." + fullOperationName(context);
        internalLogger.log(LOG_LEVEL, "using logger {0}", loggerName);
        final ADFLogger logger = ADFLogger.createADFLogger(loggerName);
        return logger;
    }

    private String fullOperationName(final MessageContext context) {
        final QName iface = (QName)context.get(MessageContext.WSDL_INTERFACE);
        final QName operation =
            (QName)context.get(MessageContext.WSDL_OPERATION);
        return iface.getLocalPart() + "." + operation.getLocalPart();
    }

    private String perfTimerName(final MessageContext context) {
        return "invoking service " + fullOperationName(context);
    }


    @Override
    public boolean handleMessage(final SOAPMessageContext context) {
//        String debugEnabled = (String)context.get("debugEnabled");
//        String logModule = (String)context.get("module");
        if (Boolean.TRUE.equals(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
            handleRequest(context);
            //String s = prettyXml(context.getMessage());
//            LogBean.writeLog(logModule, s, Constants.LOG_DEBUG,debugEnabled);
            //System.out.println("Request:" + s);
        } else {
            handleResponse(context);
            //String s = prettyXml(context.getMessage());
            //System.out.println("Response:" + s);
//            LogBean.writeLog(logModule, s, Constants.LOG_DEBUG,debugEnabled);
        }
        return true; // return true to continue processing other handlers
    }

    public void handleRequest(final SOAPMessageContext context) {
        // logging
        final ADFLogger logger = getServiceLogger(context);
        if (logger.isLoggable(LOG_LEVEL)) {
//            logger.log(LOG_LEVEL, "invoking {0} at {1}",
//                       new Object[] { fullOperationName(context),
//                                      context.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY) });
            final String payload = prettyXml(context.getMessage());
         //   logger.log(LOG_LEVEL, "request payload\n{0}", payload);
            final HashMap<String, String> logContext =
                new HashMap<String, String>();
            logContext.put("payload", payload);
            logContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                           String.valueOf(context.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY)));
            logContext.put(MessageContext.WSDL_INTERFACE,
                           String.valueOf(context.get(MessageContext.WSDL_INTERFACE)));
            logContext.put(MessageContext.WSDL_OPERATION,
                           String.valueOf(context.get(MessageContext.WSDL_OPERATION)));
            logContext.put(MessageContext.WSDL_PORT,
                           String.valueOf(context.get(MessageContext.WSDL_PORT)));
            logContext.put(MessageContext.WSDL_SERVICE,
                           String.valueOf(context.get(MessageContext.WSDL_SERVICE)));
            logger.begin(perfTimerName(context), logContext);
        } else {
            logger.begin(perfTimerName(context),
                         new HashMap<String, String>());
        }
        context.put(KEY_REQUEST, context.getMessage());
        context.put(KEY_START_TIME, System.nanoTime());
    }

    public void handleResponse(final SOAPMessageContext context) {
        final ADFLogger logger = getServiceLogger(context);
        stopTimer(context, logger);
//        if (logger.isLoggable(LOG_LEVEL)) {
//           _ logger.log(LOG_LEVEL, "response payload\n{0}",
//                       prettyXml(context.getMessage()));
//        }
    }

    @Override
    public boolean handleFault(final SOAPMessageContext context) {
        final ADFLogger logger = getServiceLogger(context);
        stopTimer(context, logger);
//        if (logger.isLoggable(ADFLogger.WARNING)) {
//            logger.log(ADFLogger.WARNING,
//                       "fault response payload\n{0}\n...caused by request\n{1}",
//                       new Object[] { prettyXml(context.getMessage()),
//                                      prettyXml((SOAPMessage)context.get(KEY_REQUEST)) });
//        }
        return true;
    }

    private void stopTimer(final SOAPMessageContext context,
                           final ADFLogger logger) {
        logger.end(perfTimerName(context)); // stop ADLogger performance timer
        final long elapsed =
            (System.nanoTime() - (Long)context.get(KEY_START_TIME)) /
            NANOSECS_IN_MILLISECS;
//        logger.log(LOG_LEVEL, "invoking {0} completed in {1} msecs",
//                   new Object[] { fullOperationName(context), elapsed });
    }

    private String prettyXml(final SOAPMessage msg) {
        if (msg == null) {
            return null;
        }
        try {
            final StringWriter sw = new StringWriter();
            final Element msgDocElem = msg.getSOAPPart().getDocumentElement();
            final XMLDocument xdoc = new XMLDocument();
            xdoc.appendChild(xdoc.importNode(msgDocElem, true));
            xdoc.print(sw);
            return sw.toString();
        } catch (Exception e) {
            internalLogger.warning(e);
            return "error pretty-printing XML: " + e.getMessage();
        }
    }
    static {
        String level = System.getProperty(SoapHandler.class.getSimpleName());
        LOG_LEVEL = level == null ? ADFLogger.TRACE : Level.parse(level);
    }
}